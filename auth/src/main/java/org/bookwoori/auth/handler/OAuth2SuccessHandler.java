package org.bookwoori.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.auth.utils.CookieUtil;
import org.bookwoori.auth.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private static final String URI = "/auth/success";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        // accessToken 발급
        Long memberId = tokenProvider.extractMemberId(authentication);
        Long kakaoId = tokenProvider.extractKakaoId(authentication);
        String accessToken = tokenProvider.generateAccessToken(authentication);
        response.addHeader("Authorization", "Bearer " + accessToken);

        // refreshToken 발급 및 쿠키에 저장
        String refreshToken = tokenProvider.generateRefreshToken(memberId, kakaoId);
        cookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken,
            CookieUtil.REFRESH_TOKEN_MAX_AGE);

        // refreshToken Redis에 저장
        tokenProvider.saveRefreshToken(memberId, refreshToken);

        // 리다이렉트 URL 설정 및 accessToken 전달
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
            .queryParam("accessToken", accessToken)
            .build().toUriString();

        // refreshToken -> /auth/refresh 엔드포인트로 요청
        response.sendRedirect(redirectUrl);
    }
}

