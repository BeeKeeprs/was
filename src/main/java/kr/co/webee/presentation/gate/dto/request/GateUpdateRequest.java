package kr.co.webee.presentation.gate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "개폐기 수정 request")
public record GateUpdateRequest(
        @Schema(description = "개폐기 이름", example = "딸기 온실 개폐기 2호")
        @NotBlank
        String name,

        @Schema(description = "지역", example = "충청북도 청주시 오창읍")
        String region,

        @Schema(description = "설치 위치", example = "온실 북쪽 출입구")
        String location,

        @Schema(description = "메모", example = "점검 완료")
        String memo
) {
}
