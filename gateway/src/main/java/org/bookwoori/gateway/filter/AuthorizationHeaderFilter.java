package org.bookwoori.gateway.filter;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.gateway.exception.CustomException;
import org.bookwoori.gateway.exception.ErrorCode;
import org.bookwoori.gateway.exception.ErrorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${jwt.secret.access}")
    private String accessSecret;
    private SecretKey secretKey;

    private final ServerSecurityContextRepository securityContextRepository;

    public AuthorizationHeaderFilter(ServerSecurityContextRepository securityContextRepository) {
        super(Config.class);
        this.securityContextRepository = securityContextRepository;
    }

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // Swagger 관련 경로 JWT 검증에서 제외
            if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/webjars") || path.startsWith("/core/v3/api-docs") || path.startsWith("/chat/v3/api-docs") || path.startsWith("/notification/v3/api-docs") || path.startsWith("/auth/v3/api-docs") ) {
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, ErrorCode.INVALID_TOKEN);
            }
            String jwt = authHeader.substring(7);

            if (!isJwtValid(jwt)) {
                return onError(exchange, ErrorCode.INVALID_TOKEN);
            }

            // Authentication 객체 생성
            String memberId = getMemberIdFromJwt(jwt);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                memberId,
                null, // password 없음
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            // SecurityContext 설정
            SecurityContext securityContext = new SecurityContextImpl(authentication);
            return securityContextRepository.save(exchange, securityContext)
                .then(chain.filter(exchange.mutate()
                    .request(request.mutate().header("memberId", memberId).build())
                    .build()));
        };
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        String subject = null;
        try {
            subject = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
        } catch (Exception ex) {
            returnValue = false;
        }
        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }
        return returnValue;
    }

    private String getMemberIdFromJwt(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
            String memberId = claims.getSubject();
            return memberId; // String 타입의 memberId
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    // 에러 처리
    private Mono<Void> onError(ServerWebExchange exchange, ErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(errorCode.getStatus()));
        ErrorDto errorDto = ErrorDto.builder()
            .timestamp(LocalDateTime.now().toString())
            .status(errorCode.getStatus())
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .path(exchange.getRequest().getURI().getPath())
            .build();

        byte[] bytes = serializeError(errorDto); // ErrorDto를 JSON으로 변환
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        return response.writeWith(Mono.just(buffer));
    }

    // JSON 변환
    private byte[] serializeError(ErrorDto errorDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsBytes(errorDto);
        } catch (JsonProcessingException e) {
            return new byte[0];
        }
    }

    public static class Config {
    }
}
