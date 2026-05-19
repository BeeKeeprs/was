package kr.co.webee.presentation.hive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "벌통 수정 request")
public record HiveUpdateRequest(
        @Schema(description = "벌통 이름", example = "딸기 벌통 2호")
        @NotBlank
        String name,

        @Schema(description = "지역", example = "충청북도 청주시 오창읍")
        @NotBlank
        String region,

        @Schema(description = "설치 위치", example = "과수원 북쪽")
        @NotBlank
        String location,

        @Schema(description = "메모", example = "월동 후 점검 완료")
        String memo
) {
}
