package kr.co.webee.common.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final Duration accessExpiration;
    private final Duration refreshExpiration;
    private final String issuer;

    private final String USER_ID_CLAIM = "userId";

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.access-expiration}") Duration accessExpiration,
                       @Value("${jwt.refresh-expiration}") Duration refreshExpiration,
                       @Value("${jwt.issuer}") String issuer) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
    }

    public String createAccessToken(String username, Long userId) {
        return createJwt(createClaims(userId), username, accessExpiration);
    }

    public String createRefreshToken(String username, Long userId) {
        return createJwt(createClaims(userId), username, refreshExpiration);
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getUserId(String token) {
        return parseClaims(token).get(USER_ID_CLAIM, Long.class);
    }

    public boolean isExpired(String token) {
        return Instant.now().isAfter(parseClaims(token).getExpiration().toInstant());
    }

    private String createJwt(Map<String, Object> claims, String subject, Duration expiration) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.now().plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    private Map<String, Object> createClaims(Long userId) {
        return Map.of(USER_ID_CLAIM, userId);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
