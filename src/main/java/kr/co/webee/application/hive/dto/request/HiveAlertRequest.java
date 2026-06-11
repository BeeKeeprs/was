package kr.co.webee.application.hive.dto.request;

import kr.co.webee.domain.hive.type.SensorType;

import java.time.LocalDateTime;

public record HiveAlertRequest(
        LocalDateTime timestamp,
        SensorType type,
        Double value,
        Double threshold
) {
}
