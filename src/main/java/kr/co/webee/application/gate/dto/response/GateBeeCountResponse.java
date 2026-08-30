package kr.co.webee.application.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.type.Period;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "개폐기 벌 카운트 조회 response")
public record GateBeeCountResponse(
        @Schema(description = "조회 기간", example = "DAY")
        Period period,

        @Schema(description = "벌 카운트 데이터 목록")
        List<DataPoint> data
) {
    public static GateBeeCountResponse of(Period period, List<DataPoint> data) {
        return GateBeeCountResponse.builder()
                .period(period)
                .data(data)
                .build();
    }

    @Builder
    @Schema(description = "벌 카운트 데이터 포인트")
    public record DataPoint(
            @Schema(description = "시간 라벨", example = "00:00")
            String label,

            @Schema(description = "입구 진입 (데이터 없을 경우 null)", nullable = true)
            Integer entranceIn,

            @Schema(description = "입구 진출 (데이터 없을 경우 null)", nullable = true)
            Integer entranceOut,

            @Schema(description = "출구 진입 (데이터 없을 경우 null)", nullable = true)
            Integer exitIn,

            @Schema(description = "출구 진출 (데이터 없을 경우 null)", nullable = true)
            Integer exitOut
    ) {
        public static DataPoint of(String label, Integer entranceIn, Integer entranceOut,
                                   Integer exitIn, Integer exitOut) {
            return DataPoint.builder()
                    .label(label)
                    .entranceIn(entranceIn)
                    .entranceOut(entranceOut)
                    .exitIn(exitIn)
                    .exitOut(exitOut)
                    .build();
        }
    }
}
