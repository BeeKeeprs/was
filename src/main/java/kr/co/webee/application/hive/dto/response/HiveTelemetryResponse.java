package kr.co.webee.application.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.type.Period;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "벌통 센서 데이터 조회 response")
public record HiveTelemetryResponse(
        @Schema(description = "조회 기간", example = "DAY")
        Period period,

        @Schema(description = "센서 데이터 목록")
        List<DataPoint> data
) {
    public static HiveTelemetryResponse of(Period period, List<DataPoint> data) {
        return HiveTelemetryResponse.builder()
                .period(period)
                .data(data)
                .build();
    }

    @Builder
    @Schema(description = "센서 데이터 포인트")
    public record DataPoint(
            @Schema(description = "시간 라벨 (DAY: HH:00, WEEK/MONTH: MM/dd)", example = "14:00")
            String label,

            @Schema(description = "내부 온도 평균 (데이터 없을 경우 null)", nullable = true)
            Double internalTemperature,

            @Schema(description = "외부 온도 평균 (데이터 없을 경우 null)", nullable = true)
            Double externalTemperature,

            @Schema(description = "내부 습도 평균 (데이터 없을 경우 null)", nullable = true)
            Double internalHumidity,

            @Schema(description = "외부 습도 평균 (데이터 없을 경우 null)", nullable = true)
            Double externalHumidity,

            @Schema(description = "CO2 농도 평균 (데이터 없을 경우 null)", nullable = true)
            Double co2,

            @Schema(description = "펠티어 모드 (슬롯 내 마지막 값, 데이터 없을 경우 null)", example = "COOL", nullable = true)
            String peltierMode,

            @Schema(description = "펠티어 듀티 평균 (%)", nullable = true)
            Double peltierDutyPct,

            @Schema(description = "핫사이드 팬 듀티 평균 (%)", nullable = true)
            Double fanHotDutyPct,

            @Schema(description = "콜드사이드 팬 듀티 평균 (%)", nullable = true)
            Double fanColdDutyPct,

            @Schema(description = "팬 상태 (슬롯 내 마지막 값, 데이터 없을 경우 null)", example = "ON", nullable = true)
            String fanState,

            @Schema(description = "목표 온도 평균 (데이터 없을 경우 null)", nullable = true)
            Double targetTemperature,

            @Schema(description = "내부 센서 유효 여부 (슬롯 내 마지막 값)", nullable = true)
            Boolean internalSensorValid,

            @Schema(description = "외부 센서 유효 여부 (슬롯 내 마지막 값)", nullable = true)
            Boolean externalSensorValid,

            @Schema(description = "펠티어 냉각 전류 평균 (A)", nullable = true)
            Double peltierCoolCurrentA,

            @Schema(description = "펠티어 가열 전류 평균 (A)", nullable = true)
            Double peltierHeatCurrentA,

            @Schema(description = "해당 구간에서 발생한 하드웨어 이슈 목록")
            List<HwIssue> issues
    ) {}

    @Schema(description = "하드웨어 이슈 이벤트")
    public record HwIssue(
            @Schema(description = "이슈 코드", example = "SENSOR_READ_FAIL")
            String code,

            @Schema(description = "이슈 발생 시각 (ISO 8601 또는 UNSYNCED_BOOT)", example = "2026-09-21T00:44:12.000Z", nullable = true)
            String timestamp
    ) {}
}
