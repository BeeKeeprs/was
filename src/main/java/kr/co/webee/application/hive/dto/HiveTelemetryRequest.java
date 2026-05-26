package kr.co.webee.application.hive.dto;

import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveTelemetry;

import java.time.LocalDateTime;

public record HiveTelemetryRequest(
        Double internalTemperature,
        Double internalHumidity,
        Double externalTemperature,
        Double externalHumidity,
        Double co2,
        LocalDateTime timestamp
) {
    public HiveTelemetry toEntity(Hive hive) {
        return HiveTelemetry.builder()
                .internalTemperature(internalTemperature)
                .internalHumidity(internalHumidity)
                .externalTemperature(externalTemperature)
                .externalHumidity(externalHumidity)
                .co2(co2)
                .recordedAt(timestamp)
                .hive(hive)
                .build();
    }
}