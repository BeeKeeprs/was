package kr.co.webee.presentation.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "개폐기 등록 response")
public record GateRegisterResponse(
        @Schema(description = "개폐기 ID", example = "1")
        Long gateId
) {
    public static GateRegisterResponse of(Long gateId) {
        return GateRegisterResponse.builder()
                .gateId(gateId)
                .build();
    }
}
