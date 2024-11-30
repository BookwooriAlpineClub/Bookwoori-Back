package org.bookwoori.auth.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.auth.global.utils.CookieUtil;
import org.bookwoori.auth.global.jwt.TokenProvider;
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
    private static final String SUCCESS_URI = "https://www.bookwoori.site/sign-in";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        // accessToken 발급
        Long memberId = tokenProvider.extractMemberId(authentication);
        Long kakaoId = tokenProvider.extractKakaoId(authentication);
        String accessToken = tokenProvider.generateAccessToken(authentication);

        // refreshToken 발급 및 쿠키에 저장
        String refreshToken = tokenProvider.generateRefreshToken(memberId, kakaoId);
        cookieUtil.addCookieAndSetDomain(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken,
            CookieUtil.REFRESH_TOKEN_MAX_AGE, ".bookwoori.site");
        cookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken,
            CookieUtil.REFRESH_TOKEN_MAX_AGE);

        // Refresh Token Redis에 저장
        tokenProvider.saveRefreshToken(kakaoId, refreshToken);

        response.sendRedirect(SUCCESS_URI);

//        // JSON 형태로 응답
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(HttpServletResponse.SC_OK); // 200 OK 상태 설정
//        response.getWriter().write("{\"message\": \"Authentication successful\", " +
//            "\"accessToken\": \"" + accessToken + "\", " +
//            "\"refreshToken\": \"" + refreshToken + "\"}");
    }
}

