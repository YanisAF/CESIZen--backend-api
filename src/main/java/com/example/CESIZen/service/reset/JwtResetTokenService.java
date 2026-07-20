package com.example.CESIZen.service.reset;

import com.example.CESIZen.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtResetTokenService {

    private final Key key;

    public JwtResetTokenService(@Value("${jwt.secret-reset}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateResetToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", "PASSWORD_RESET")
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 60 * 1000)
                )
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}
