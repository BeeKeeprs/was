package kr.co.webee.application.hive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.domain.hive.type.SensorType;
import lombok.Builder;

import java.util.List;

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
            Double value
    ) {
        public static DataPoint of(String label, Double value) {
            return DataPoint.builder()
                    .label(label)
                    .value(value)
                    .build();
        }
    }
}
