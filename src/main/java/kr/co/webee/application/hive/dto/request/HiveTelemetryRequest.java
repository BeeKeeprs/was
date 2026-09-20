package kr.co.webee.application.hive.dto.request;

import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveTelemetry;

import java.time.LocalDateTime;

public record HiveTelemetryRequest(
        Double internalTemperature,
        Double internalHumidity,
        Double externalTemperature,
        Double externalHumidity,
        Double co2,
        String peltierMode,
        Double peltierDutyPct,
        Double fanHotDutyPct,
        Double fanColdDutyPct,
        String fanState,
        Double targetTemperature,
        LocalDateTime timestamp
) {
    public HiveTelemetry toEntity(Hive hive) {
        return HiveTelemetry.builder()
                .internalTemperature(internalTemperature)
                .internalHumidity(internalHumidity)
                .externalTemperature(externalTemperature)
                .externalHumidity(externalHumidity)
                .co2(co2)
                .peltierMode(peltierMode)
                .peltierDutyPct(peltierDutyPct)
                .fanHotDutyPct(fanHotDutyPct)
                .fanColdDutyPct(fanColdDutyPct)
                .fanState(fanState)
                .targetTemperature(targetTemperature)
                .recordedAt(timestamp)
                .hive(hive)
                .build();
    }
}