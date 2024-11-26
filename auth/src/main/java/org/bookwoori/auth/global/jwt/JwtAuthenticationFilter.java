package org.bookwoori.auth.global.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.bookwoori.auth.global.exception.ErrorCode;
import org.bookwoori.auth.global.exception.TokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);
        if (accessToken != null) {
            try {
                if (tokenProvider.validateToken(accessToken, false)) {
                    Authentication authentication = tokenProvider.getAuthentication(accessToken,
                        false);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new TokenException(ErrorCode.INVALID_TOKEN);
                }
            } catch (TokenException e) {
                // TokenException을 던져서 TokenExceptionFilter에서 처리하게 함
                throw e;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken, false);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
}
