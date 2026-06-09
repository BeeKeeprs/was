package kr.co.webee.application.sse.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseEventType {
    CONNECT("SSE 연결"),
    HIVE_CONTROL_RESULT("벌통 제어 결과"),
    TEST("테스트용");

    private final String value;
}
