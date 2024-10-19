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
        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else { // accessToken이 유효하지 않은 경우 처리
            handleInvalidToken(request, response, filterChain);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleInvalidToken(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 새로운 Access Token 발급
            String newAccessToken = tokenProvider.generateAccessToken(authentication);
            // 새로운 Access Token으로 인증 정보 업데이트
            setAuthentication(newAccessToken);
            // 응답 헤더에 새로운 Access Token 추가
            response.setHeader(AUTHORIZATION, "Bearer " + newAccessToken);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }
}

