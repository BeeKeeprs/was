package kr.co.webee.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean containsKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}