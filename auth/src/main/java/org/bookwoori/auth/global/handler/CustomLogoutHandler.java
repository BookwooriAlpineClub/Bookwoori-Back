package org.bookwoori.auth.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bookwoori.auth.global.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenProvider tokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        String refreshToken = request.getHeader("refreshToken");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            // Redis에서 Refresh Token 삭제
            tokenProvider.deleteRefreshToken(refreshToken);
        }

    }
}
