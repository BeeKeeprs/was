package kr.co.webee.application.hive.dto.request;

import kr.co.webee.domain.hive.type.SensorType;

import java.time.Instant;

public record HiveAlertRequest(
        Instant timestamp,
        SensorType type,
        Double value,
        Double threshold
) {
}
