package org.bookwoori.auth.global.service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;

    private final SecretKey secretKey;

    /**
     * AuthService 생성자.
     * Base64 URL 인코딩된 비밀 키를 디코딩하여 HMAC-SHA 알고리즘에 적합한 SecretKey 객체 생성
     * @param secretKey Base64 URL 인코딩된 비밀 키
     */
    public AuthService(@Value("${jwt.secret.access}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public String createAccessToken(Long memberId){
        Date now = new Date();
        return Jwts.builder()
            // 헤더 - 토큰 타입: JWT
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            // 내용 - 토큰이 발급된 시간: 현재 시간
            .setIssuedAt(now)
            // 내용 - 토큰 만료 시간: expiredMs 변수값
            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
            // 서명 - 시크릿키와 함께 해시값을 HS256 방식으로 암호화
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
}
