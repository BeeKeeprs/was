package kr.co.webee.application.hive.dto.response;

import kr.co.webee.domain.hive.entity.HiveTelemetry;

import java.time.LocalDateTime;

public record HiveTelemetrySseResponse(
        Long hiveId,
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
        LocalDateTime recordedAt
) {
    public static HiveTelemetrySseResponse from(HiveTelemetry telemetry) {
        return new HiveTelemetrySseResponse(
                telemetry.getHive().getId(),
                telemetry.getInternalTemperature(),
                telemetry.getInternalHumidity(),
                telemetry.getExternalTemperature(),
                telemetry.getExternalHumidity(),
                telemetry.getCo2(),
                telemetry.getPeltierMode(),
                telemetry.getPeltierDutyPct(),
                telemetry.getFanHotDutyPct(),
                telemetry.getFanColdDutyPct(),
                telemetry.getFanState(),
                telemetry.getTargetTemperature(),
                telemetry.getRecordedAt()
        );
    }
}
