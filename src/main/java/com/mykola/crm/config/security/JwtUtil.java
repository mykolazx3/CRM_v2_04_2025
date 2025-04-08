package com.mykola.crm.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.expiration.time}")
    private int expiration;
    private Key secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secretString) {
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ask", "ask1");

        return Jwts.builder()
                .setSubject(username) // Встановлюємо стандартний клейм "subject" з іменем користувача
                .addClaims(claims)  // Додаємо ваші власні клейми
                .setIssuer("CRM")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * expiration)))  // було
                .signWith(secretKey).compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Витягуємо ім'я користувача зі стандартного клейма "subject"
    }

    public boolean isValidToken(String token) {

        try {
            return !Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().before(new Date());

        } catch (JwtException e) {
            throw new RuntimeException("Expired or invalid JWT token", e);
        }
    }

}