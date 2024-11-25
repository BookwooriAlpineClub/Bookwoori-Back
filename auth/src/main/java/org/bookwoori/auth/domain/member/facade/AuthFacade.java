package org.bookwoori.auth.domain.member.facade;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.auth.domain.member.dto.TokenRequestDto;
import org.bookwoori.auth.global.exception.TokenException;
import org.bookwoori.auth.global.feignClient.CoreClient;
import org.bookwoori.auth.global.jwt.TokenProvider;
import org.bookwoori.auth.global.exception.ErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class AuthFacade {
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public Map<String, String> refreshAccessToken(String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(refreshToken,
            true);
        Long kakaoId = tokenProvider.extractKakaoId(authentication);
        // Redis에서 kakaoId를 key로 하는 refreshToken 가져옴
        String storedRefreshToken = redisTemplate.opsForValue()
            .get(kakaoId.toString());
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
        Map<String, String> tokens = tokenProvider.renewAccessAndRefreshToken(
            refreshToken);
        String newRefreshToken = tokens.get("refreshToken");
        // 새로운 refreshToken Redis에 설정
        tokenProvider.saveRefreshToken(kakaoId, newRefreshToken);
        return tokens;
    }
}
