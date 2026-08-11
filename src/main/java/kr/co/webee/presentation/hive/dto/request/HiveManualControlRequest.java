package kr.co.webee.presentation.hive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "벌통 제어 목표값 설정 request")
public record HiveManualControlRequest(
        @Schema(description = "목표 온도 (°C)", example = "25.0")
        Double targetTemperature,

        @Schema(description = "목표 습도 (%)", example = "60.0")
        Double targetHumidity
) {
}
