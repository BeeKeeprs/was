package kr.co.webee.application.mqtt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MqttTopicType {
    TELEMETRY("telemetry"),
    CONNECTION("connection"),
    COMMAND_RESPONSE("command/response"),
    BEE_COUNT("bee-count"),
    ALERT("alert");

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
