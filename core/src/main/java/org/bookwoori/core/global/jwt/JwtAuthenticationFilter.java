package org.bookwoori.core.global.jwt;

import org.bookwoori.core.global.oauth.OAuth2UserInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);
        // accessToken 검증
        if (accessToken != null && tokenProvider.validateToken(accessToken, false)) {
            setAuthentication(accessToken);
        } else if (accessToken != null) {
            // accessToken 만료 시 클라이언트에게 재발급 요청하도록 응답 설정
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired. Please refresh aceeesToken&refreshToken.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
}


