package org.bookwoori.auth.domain.member.facade;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
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


    public Map<String, String> login(String refreshToken){
        return renewAccessAndRefreshToken(refreshToken);
    }


    public Map<String, String> refreshAccessToken(String refreshToken){
        return renewAccessAndRefreshToken(refreshToken);
    }

    private Map<String, String> renewAccessAndRefreshToken(String refreshToken){
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new TokenException(ErrorCode.NO_COOKIE); // 쿠키가 없는 경우
        }
        Authentication authentication;
        try {
            authentication = tokenProvider.getAuthentication(refreshToken, true);
        } catch (ExpiredJwtException e) {
            throw new TokenException(ErrorCode.EXPIRED_REFRESH_TOKEN); // 토큰 만료 에러
        } catch (SignatureException e) {
            throw new TokenException(ErrorCode.INVALID_JWT_SIGNATURE); // 서명이 잘못된 경우
        } catch (Exception e) {
            throw new TokenException(ErrorCode.INVALID_TOKEN); // 기타 유효하지 않은 토큰
        }
        Long kakaoId = tokenProvider.extractKakaoIdFromToken(refreshToken);

        // Redis에서 refreshToken을 조회
        String storedRefreshToken = redisTemplate.opsForValue().get(kakaoId.toString());
        if (storedRefreshToken == null) {
            throw new TokenException(ErrorCode.TOKEN_NOT_FOUND); // 저장된 토큰 없음
        }
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new TokenException(ErrorCode.INVALID_TOKEN); // 저장된 토큰과 일치하지 않음
        }

        // 새로운 accessToken과 refreshToken 생성
        Map<String, String> tokens = tokenProvider.renewAccessAndRefreshToken(refreshToken);
        String newRefreshToken = tokens.get("refreshToken");

        // Redis에 새로운 refreshToken 저장
        tokenProvider.saveRefreshToken(kakaoId, newRefreshToken);

        return tokens;
    }
}
