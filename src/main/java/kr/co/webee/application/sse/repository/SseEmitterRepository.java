package kr.co.webee.application.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public List<SseEmitter> findAllByUserId(Long userId) {
        return emitters.getOrDefault(userId, List.of());
    }

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        emitters.computeIfAbsent(userId, k -> new ArrayList<>()).add(sseEmitter);
        return sseEmitter;
    }

    public void delete(Long userId, SseEmitter sseEmitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);

        if (userEmitters != null) {
            userEmitters.remove(sseEmitter);

            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }
}
