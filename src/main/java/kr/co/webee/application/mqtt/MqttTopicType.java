package kr.co.webee.application.mqtt;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MqttTopicType {
    TELEMETRY("hive", "telemetry"),
    CONNECTION("hive", "connection"),
    ALERT("hive", "alert"),
    CONTROL_RESPONSE("hive", "control/response"),
    GATE_TELEMETRY("gate", "telemetry"),
    GATE_BEE_COUNT("gate", "bee-count"),
    GATE_TUNNEL_ALERT("gate", "tunnel-alert");

    private final String prefix;
    private final String suffix;

    MqttTopicType(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public static MqttTopicType from(String topic) {
        return Arrays.stream(values())
                .filter(type -> type.matches(topic))
                .findFirst()
                .orElse(null);
    }

    private boolean matches(String topic) {
        return topic.startsWith(prefix + "/") && topic.endsWith("/" + suffix);
    }
}
