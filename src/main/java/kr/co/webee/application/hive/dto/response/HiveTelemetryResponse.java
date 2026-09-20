package kr.co.webee.application.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.domain.hive.type.SensorType;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
@Schema(description = "벌통 센서 데이터 조회 response")
public record HiveTelemetryResponse(
        @Schema(description = "센서 타입", example = "INTERNAL_TEMPERATURE")
        SensorType sensorType,

        @Schema(description = "조회 기간", example = "DAY")
        Period period,

        @Schema(description = "센서 데이터 목록")
        List<DataPoint> data
) {
    public static HiveTelemetryResponse of(SensorType sensorType, Period period, List<DataPoint> data) {
        return HiveTelemetryResponse.builder()
                .sensorType(sensorType)
                .period(period)
                .data(data)
                .build();
    }

    @Builder
    @Schema(description = "센서 데이터 포인트")
    public record DataPoint(
            @Schema(description = "시간 라벨 (DAY: HH:00, WEEK/MONTH: MM/dd)", example = "00:00")
            String label,

            @Schema(description = "센서 측정값 (데이터 없을 경우 null)", example = "34.5", nullable = true)
            Double value,

            @Schema(description = "해당 구간에서 발생한 하드웨어 이슈 목록")
            List<HwIssue> issues
    ) {
        public static DataPoint of(String label, Double value, List<HwIssue> issues) {
            return DataPoint.builder()
                    .label(label)
                    .value(value)
                    .issues(issues)
                    .build();
        }
    }

    @Schema(description = "하드웨어 이슈 이벤트")
    public record HwIssue(
            @Schema(description = "이슈 코드", example = "SENSOR_READ_FAIL")
            String code,

            @Schema(description = "이슈 발생 시각 (ISO 8601 또는 UNSYNCED_BOOT)", example = "2026-09-21T00:44:12.000Z", nullable = true)
            String timestamp
    ) {}
}
