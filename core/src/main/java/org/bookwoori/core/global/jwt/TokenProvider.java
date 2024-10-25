package org.bookwoori.core.global.jwt;

import com.nimbusds.oauth2.sdk.token.Tokens;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

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
    private final JwtService jwtService;

    @PostConstruct
    private void setSecretKey() {
        accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME, accessKey, "access");
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME, refreshKey, "refresh");
    }

    private String generateToken(Authentication authentication, long tokenExpireTime, SecretKey key, String tokenType) {
        Long kakaoId = extractKakaoId(authentication); // Authentication 객체에서 kakaoId 추출
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpireTime);

        // 권한 목록 설정
        String authorityList = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        JwtBuilder builder = Jwts.builder()
                .setSubject(String.valueOf(kakaoId))
                .claim("type", tokenType)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512);

        // 액세스 토큰에만 권한 정보 추가
        if (!authorityList.isEmpty() && "access".equals(tokenType)) {
            builder.claim("role", authorityList);
        }

        return builder.compact();
    }

    public Map<String, String> refreshTokens(String refreshToken) {
        if (validateToken(refreshToken, true)) {
            Claims claims = parseClaims(refreshToken, refreshKey);
            Long kakaoId = Long.valueOf(claims.getSubject());
            // 해당 사용자 ID로 새로운 Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoId, null, getAuthorities(claims));
            // 새 accessToken 및 refreshToken 생성
            String newAccessToken = generateAccessToken(authentication);
            String newRefreshToken = generateRefreshToken(authentication);
            // 결과를 Map에 담아 반환
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);
            return tokens;
        }
        throw new TokenException(ErrorCode.INVALID_TOKEN);
    }

    private Long extractKakaoId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            return Long.valueOf(oAuth2User.getAttributes().get("id").toString());  // 카카오 ID 추출
        }
        throw new TokenException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public boolean validateToken(String token, boolean isRefreshToken) {
        try {
            Claims claims = parseClaims(token, isRefreshToken ? refreshKey : accessKey);
            return claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException e) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token, accessKey);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        String roles = claims.get("role", String.class);
        return roles == null ? List.of(new SimpleGrantedAuthority("ROLE_USER"))
                : List.of(new SimpleGrantedAuthority(roles));
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
