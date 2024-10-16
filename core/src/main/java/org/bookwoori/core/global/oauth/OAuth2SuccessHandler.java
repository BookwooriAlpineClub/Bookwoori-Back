package org.bookwoori.core.global.oauth;

import org.bookwoori.core.global.jwt.TokenProvider;
import org.bookwoori.core.global.jwt.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final String URI = "/auth/success";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);

        // 헤더 Authorization에 Bearer Token 담기
        response.addHeader("Authorization", "Bearer " + accessToken);

        // refresh 토큰 발급 및 쿠키에 저장
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        cookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, CookieUtil.REFRESH_TOKEN_MAX_AGE);

        // PrincipalDetails를 사용하여 새로운 Authentication 객체 생성
        if (authentication.getPrincipal() instanceof PrincipalDetails principalDetails) {
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    principalDetails,
                    null,
                    principalDetails.getAuthorities()
            );

            // 새로운 Authentication 객체를 SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } else {
            throw new IllegalArgumentException("Authentication principal is not of type PrincipalDetails");
        }

        // 리다이렉트 URL 설정 및 accessToken 전달
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}

