package org.bookwoori.core.global.jwt;

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
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private static final String KEY_ROLE = "role";
    private static final String KEY_TYPE = "type";
    private final JwtService jwtService;

    @PostConstruct
    private void setSecretKey() {
        accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }


    public String generateAccessToken(Authentication authentication) {
        Long kakaoId = extractKakaoId(authentication);
        Member member = jwtService.getOrCreateMemberByKakaoId(kakaoId);
        String currentToken = member.getAccessToken();

        // 만약 현재 액세스 토큰이 유효하다면, 새로운 토큰을 발급하지 않음
        if (currentToken != null && validateToken(currentToken)) {
            return currentToken;
        }

        String newAccessToken = generateToken(kakaoId, authentication.getAuthorities(),
            ACCESS_TOKEN_EXPIRE_TIME, accessKey, "access");
        jwtService.saveOrUpdateAccessToken(member, newAccessToken);
        return newAccessToken;
    }

    public String generateRefreshToken(Authentication authentication) {
        Long kakaoId = extractKakaoId(authentication);

        // refreshToken을 새로 생성하여 저장
        String refreshToken = generateToken(kakaoId, Collections.emptyList(),
            REFRESH_TOKEN_EXPIRE_TIME, refreshKey, "refresh");
        return refreshToken;
    }

    private String generateToken(Long kakaoId, Collection<? extends GrantedAuthority> authorities,
        long tokenExpireTime, SecretKey key, String tokenType) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + tokenExpireTime);

        String authorityList = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining());

        JwtBuilder builder = Jwts.builder()
            .subject(String.valueOf(kakaoId))  // 토큰에 카카오 ID를 subject로 설정
            .claim(KEY_TYPE, tokenType)  // 토큰 타입 (access / refresh)
            .issuedAt(now)
            .setExpiration(expiredDate)
            .signWith(key, SignatureAlgorithm.HS512);

        // 만약 권한 정보가 존재한다면, 액세스 토큰에만 추가
        if (!authorityList.isEmpty() && "access".equals(tokenType)) {
            builder.claim(KEY_ROLE, authorityList);
        }

        return builder.compact();
    }

    private Long extractKakaoId(Authentication authentication) throws CustomException {
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            return Long.valueOf(oAuth2User.getAttributes().get("id").toString());  // 카카오에서 전달된 ID
        }
        throw new TokenException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        String role = claims.get(KEY_ROLE, String.class);
        if (role == null || role.isEmpty()) {
            role = "ROLE_USER";  // 기본 권한 설정
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(token.contains("refresh") ? refreshKey : accessKey)
                .build()
                .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
    }
}