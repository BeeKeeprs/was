package kr.co.webee.application.sse.service;

import kr.co.webee.application.sse.repository.SseEmitterRepository;
import kr.co.webee.application.sse.type.SseEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseEmitterService {
    public static final String SSE_CONNECT_DATA = "connected!";
    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Long userId) {
        SseEmitter sseEmitter = sseEmitterRepository.save(userId, new SseEmitter());

        sseEmitter.onCompletion(() -> sseEmitterRepository.delete(userId, sseEmitter));

        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            sseEmitterRepository.delete(userId, sseEmitter);
        });

        sendToClient(SseEventType.CONNECT, userId, SSE_CONNECT_DATA);

        return sseEmitter;
    }

    public void sendToClient(SseEventType eventType, Long userId, Object data) {
        List<SseEmitter> userEmitters = sseEmitterRepository.findAllByUserId(userId);

        if (userEmitters.isEmpty()) {
            log.debug("SSE emitter가 없습니다. userId={}, eventType={}", userId, eventType);
            return;
        }

        for (SseEmitter sseEmitter : List.copyOf(userEmitters)) {
            try {
                sseEmitter.send(
                        SseEmitter.event()
                                .name(eventType.name())
                                .data(data)
                );
            } catch (IOException ex) {
                log.debug("SSE 연결 종료. userId={}", userId);
                sseEmitter.complete();
                sseEmitterRepository.delete(userId, sseEmitter);
            }
        }
    }
}
