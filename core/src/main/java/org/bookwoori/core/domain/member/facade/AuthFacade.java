package org.bookwoori.core.domain.member.facade;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.TokenRequestDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.exception.TokenException;
import org.bookwoori.core.global.jwt.TokenProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class AuthFacade {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;


    public void deleteMember() {
        Member currentMember = memberService.getCurrentMember();
        currentMember.deleteMember();
    }

    public Map<String, String> refreshAccessToken(TokenRequestDto requestDto) {
        Authentication authentication = tokenProvider.getAuthentication(requestDto.refreshToken(),
            true);
        Long kakaoId = tokenProvider.extractKakaoId(authentication);
        // Redis에서 kakaoId를 key로 하는 refreshToken 가져옴
        String storedRefreshToken = redisTemplate.opsForValue()
            .get(kakaoId.toString());
        // 전달받은 리프레시 토큰과 Redis에 저장된 리프레시 토큰이 일치하는지 확인
        if (storedRefreshToken == null || !storedRefreshToken.equals(requestDto.refreshToken())) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
        // accessToken과 refreshToken을 모두 재발급
        Map<String, String> tokens = tokenProvider.renewAccessAndRefreshToken(
            requestDto.refreshToken());
        String newRefreshToken = tokens.get("refreshToken");
        // 새로운 refreshToken을 Redis에 설정
        tokenProvider.saveRefreshToken(kakaoId, newRefreshToken);
        return tokens;
    }
}
