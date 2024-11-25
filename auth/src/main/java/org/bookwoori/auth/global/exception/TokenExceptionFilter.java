package org.bookwoori.auth.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
public class TokenExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환을 위한 ObjectMapper

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            log.error("TokenException occurred: {}", e.getMessage(), e);

            ErrorCode errorCode = e.getErrorCode();

            // ErrorDto 생성
            ErrorDto errorDto = ErrorDto.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .build();

            // HTTP 응답 설정
            response.setStatus(errorDto.getStatus());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 에러 정보를 JSON으로 변환하여 응답에 작성
            String responseJson = objectMapper.writeValueAsString(errorDto);
            response.getWriter().write(responseJson);
        }
    }

}
