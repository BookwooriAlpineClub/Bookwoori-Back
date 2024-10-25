package org.bookwoori.core.global.oauth;

import org.bookwoori.core.global.jwt.TokenProvider;
import org.bookwoori.core.global.jwt.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final AuthService authService;
    private static final String URI = "/auth/success";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        response.addHeader("Authorization", "Bearer " + accessToken);

        // refreshToken 발급 및 쿠키에 저장
        Long kakoId = tokenProvider.extractKakaoId(authentication);
        authService.getMemberStatus(kakoId); // 계정 삭제한 멤버 예외 처리
        String refreshToken = tokenProvider.generateRefreshToken(kakoId);
        cookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, CookieUtil.REFRESH_TOKEN_MAX_AGE);

        // 리다이렉트 URL 설정 및 accessToken 전달
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        // refreshToken -> /auth/refresh 엔드포인트로 요청
        response.sendRedirect(redirectUrl);
    }
}


