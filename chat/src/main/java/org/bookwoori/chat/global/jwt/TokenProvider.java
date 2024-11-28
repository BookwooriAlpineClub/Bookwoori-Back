package org.bookwoori.chat.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.chat.global.exception.CustomException;
import org.bookwoori.chat.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class TokenProvider {

    @Value("${jwt.secret.access}")
    private String secret;
    private SecretKey secretKey;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getMemberId(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
