package org.bookwoori.auth.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.auth.domain.member.dto.LoginResponseDto;
import org.bookwoori.auth.domain.member.dto.TokenRequestDto;
import org.bookwoori.auth.domain.member.facade.AuthFacade;
import org.bookwoori.auth.global.exception.ErrorCode;
import org.bookwoori.auth.global.exception.TokenException;
import org.bookwoori.auth.global.jwt.TokenProvider;
import org.bookwoori.auth.global.utils.CookieUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthFacade authFacade;
    private final CookieUtil cookieUtil;

    @Operation(summary = "로그인 성공", description = "카카오 로그인에 성공합니다.")
    @GetMapping("/success")
    public ResponseEntity<?> loginSuccess(@Valid LoginResponseDto loginResponseDto) {
        return ResponseEntity.ok(loginResponseDto);
    }

    @Operation(summary = "토큰 재발급", description = "액세스 토큰 및 리프레쉬 토큰을 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody TokenRequestDto requestDto,
        HttpServletResponse response) {
        Map<String, String> tokens = authFacade.refreshAccessToken(requestDto);
        String newAccessToken = tokens.get("accessToken");
        String newRefreshToken = tokens.get("refreshToken");

        // 새로운 refreshToken 쿠키에 저장
        cookieUtil.addCookie(response, "refreshToken", newRefreshToken,
            CookieUtil.REFRESH_TOKEN_MAX_AGE);
        return ResponseEntity.ok()
            .header("Authorization", "Bearer " + newAccessToken)
            .body("New access and refresh tokens issued");
    }

    @Operation(summary = "로그아웃", description = "로그아웃 및 리프레쉬 토큰 삭제")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
        @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
        return ResponseEntity.ok().build();
    }

}

