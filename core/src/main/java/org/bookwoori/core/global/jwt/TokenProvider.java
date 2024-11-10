package org.bookwoori.core.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.secret.access}")
    private String accessSecret;
    @Value("${jwt.secret.refresh}")
    private String refreshSecret;
    private SecretKey accessKey;
    private SecretKey refreshKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;

    @PostConstruct
    private void setSecretKey() {
        accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        Long kakaoId = extractKakaoId(authentication);
        return generateToken(kakaoId, roles, ACCESS_TOKEN_EXPIRE_TIME, accessKey, "access");
    }

    public String generateRefreshToken(Long kakaoId) {
        return generateToken(kakaoId, Collections.emptyList(), REFRESH_TOKEN_EXPIRE_TIME,
            refreshKey, "refresh");
    }

    private String generateToken(Long kakaoId, List<String> roles, long tokenExpireTime,
        SecretKey key, String tokenType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpireTime);

        JwtBuilder builder = Jwts.builder()
            .setSubject(String.valueOf(kakaoId))
            .claim("type", tokenType)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512);

        if (!roles.isEmpty() && "access".equals(tokenType)) {
            builder.claim("role", String.join(",", roles));
        }

        return builder.compact();
    }

    public Map<String, String> renewAccessAndRefreshToken(String refreshToken) {
        if (validateToken(refreshToken, true)) {
            Claims claims = parseClaims(refreshToken, refreshKey);
            Long kakaoId = Long.valueOf(claims.getSubject());
            // 새 accessToken 및 refreshToken 생성
            List<String> roles = getRolesFromClaims(claims);
            String newAccessToken = generateToken(kakaoId, roles, ACCESS_TOKEN_EXPIRE_TIME,
                accessKey, "access");
            String newRefreshToken = generateRefreshToken(kakaoId);
            // 결과를 Map에 담아 반환
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);
            return tokens;
        }
        throw new TokenException(ErrorCode.INVALID_TOKEN);
    }

    public Long extractKakaoId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            return Long.valueOf(oAuth2User.getAttributes().get("id").toString());
        }
        throw new TokenException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public boolean validateToken(String token, boolean isRefreshToken) {
        try {
            Claims claims = parseClaims(token, isRefreshToken ? refreshKey : accessKey);
            return claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            if (isRefreshToken) {
                throw new TokenException(ErrorCode.EXPIRED_REFRESH_TOKEN);
            } else {
                throw new TokenException(ErrorCode.EXPIRED_ACCESS_TOKEN);
            }
        } catch (JwtException e) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token, accessKey);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
    }

    private List<String> getRolesFromClaims(Claims claims) {
        String roles = claims.get("role", String.class);
        return roles == null ? List.of("ROLE_USER") : List.of(roles.split(","));
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return getRolesFromClaims(claims).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    private Claims parseClaims(String token, SecretKey key) {
        try {
            return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
    }
}


