package kr.co.webee.domain.gate.type;

import kr.co.webee.domain.gate.entity.GateTelemetry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum GateSensorType {
    TEMPERATURE("온도"),
    HUMIDITY("습도");

    private final String description;

    public Double extract(GateTelemetry telemetry) {
        return switch (this) {
            case TEMPERATURE -> telemetry.getTemperature();
            case HUMIDITY -> telemetry.getHumidity();
        };
    }
}
