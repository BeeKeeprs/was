package kr.co.webee.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final String issuer;

    public JwtProvider(@Value("${spring.jwt.secret}") String secretKey,
                       @Value("${spring.jwt.access-expiration}") long accessExpiration,
                       @Value("${spring.jwt.refresh-expiration}") long refreshExpiration,
                       @Value("${spring.jwt.issuer}") String issuer) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
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
        return parseClaims(token).get("userId", Long.class);
    }

    public boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    private String createJwt(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    private Map<String, Object> createClaims(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return claims;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}