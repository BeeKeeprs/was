package kr.co.webee.presentation.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사전예약 여부 확인 response")
public record PreOrderCheckResponse(
        @Schema(description = "사전예약 여부", example = "true")
        boolean isPreOrdered
) {
        public static PreOrderCheckResponse of(boolean isPreOrdered) {
                return new PreOrderCheckResponse(isPreOrdered);
        }
}
