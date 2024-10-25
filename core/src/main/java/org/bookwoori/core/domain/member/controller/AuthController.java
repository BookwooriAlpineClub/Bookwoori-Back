package org.bookwoori.core.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.response.LoginResponseDto;
import org.bookwoori.core.global.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;

    @GetMapping("/success")
    public ResponseEntity<?> loginSuccess(@Valid LoginResponseDto loginResponseDto) {
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken") String refreshToken) {
        try {
            // accessToken과 refreshToken을 모두 재발급
            Map<String, String> tokens = tokenProvider.refreshTokens(refreshToken);
            String newAccessToken = tokens.get("accessToken");
            String newRefreshToken = tokens.get("refreshToken");
            // 새로운 refreshToken을 쿠키에 설정
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .httpOnly(true)
                    .secure(true)  // HTTPS 환경에서만 전송
                    .path("/")
                    .maxAge(TokenProvider.REFRESH_TOKEN_EXPIRE_TIME / 1000)  // 만료 시간 설정 (초 단위)
                    .build();
            // 응답: accessToken은 Authorization 헤더에, refreshToken은 쿠키에 설정
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .header("Set-Cookie", refreshTokenCookie.toString())
                    .body("New access and refresh tokens issued");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

}
