package kr.co.webee.application.auth.service;

import kr.co.webee.common.util.jwt.JwtConstants;

import kr.co.webee.infrastructure.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisService redisService;

    @Value("${spring.jwt.refresh-expiration}")
    private long refreshExpiration;

    public void save(Long userId, String refreshToken) {
        String key = getRefreshTokenKey(userId);
        redisService.set(key, refreshToken, Duration.ofSeconds(refreshExpiration));
    }

    public void delete(Long userId) {
        String key = getRefreshTokenKey(userId);
        redisService.delete(key);
    }

    public boolean existsByUserId(Long userId) {
        String key = getRefreshTokenKey(userId);
        return redisService.containsKey(key);
    }

    private String getRefreshTokenKey(Long userId) {
        return JwtConstants.REFRESH_TOKEN_REDIS_KEY_PREFIX + userId;
    }
}
