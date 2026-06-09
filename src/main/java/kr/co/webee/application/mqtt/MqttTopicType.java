package kr.co.webee.application.mqtt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MqttTopicType {
    TELEMETRY("telemetry"),
    CONNECTION("connection"),
    BEE_COUNT("bee-count"),
    ALERT("alert"),
    CONTROL_RESPONSE("control/response");

    private final String suffix;

    public static MqttTopicType from(String topic) {
        return Arrays.stream(values())
                .filter(type -> type.matches(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("topic: %s이 존재하지 않습니다.".formatted(topic)));
    }

    private boolean matches(String topic) {
        return topic.endsWith(suffix);
    }
}
