package org.bookwoori.core.global.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.facade.AuthFacade;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.global.jwt.CookieUtil;
import org.bookwoori.core.global.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private static final String URI = "http://localhost:3000/auth/success";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private final MemberService memberService;
    private final AuthFacade authFacade;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        // accessToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        response.addHeader("Authorization", "Bearer " + accessToken);

        // refreshToken 발급 및 쿠키에 저장
        Long kakaoId = tokenProvider.extractKakaoId(authentication);
        memberService.validateMemberStatus(kakaoId); // 계정 삭제한 멤버 예외 처리
        String refreshToken = tokenProvider.generateRefreshToken(kakaoId);
        cookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken,
            CookieUtil.REFRESH_TOKEN_MAX_AGE);
        // refreshToken Redis에 저장
        tokenProvider.saveRefreshToken(kakaoId, refreshToken);

//        // 리다이렉트 URL 설정 및 accessToken 전달
//        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
//            .queryParam("accessToken", accessToken)
//            .build().toUriString();
//
//        // refreshToken -> /auth/refresh 엔드포인트로 요청
//        response.sendRedirect(redirectUrl);
//

        // JSON형식 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // AccessToken 토큰 전달
        String jsonResponse = String.format("{\"accessToken\":\"%s\"}", accessToken);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}


