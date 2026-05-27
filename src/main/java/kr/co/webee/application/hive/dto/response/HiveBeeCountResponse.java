package kr.co.webee.application.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.type.Period;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "벌통 벌 개수 조회 response")
public record HiveBeeCountResponse(
        @Schema(description = "조회 기간", example = "DAY")
        Period period,

        @Schema(description = "벌 개수 데이터 목록")
        List<DataPoint> data
) {
    public static HiveBeeCountResponse of(Period period, List<DataPoint> data) {
        return HiveBeeCountResponse.builder()
                .period(period)
                .data(data)
                .build();
    }

    @Builder
    @Schema(description = "벌 개수 데이터 포인트")
    public record DataPoint(
            @Schema(description = "시간 라벨 (DAY: HH:00, WEEK/MONTH: MM/dd)", example = "00:00")
            String label,

            @Schema(description = "벌 개수 평균 (데이터 없을 경우 null)", example = "128", nullable = true)
            Integer value
    ) {
        public static DataPoint of(String label, Integer value) {
            return DataPoint.builder()
                    .label(label)
                    .value(value)
                    .build();
        }
    }
}
