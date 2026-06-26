package kr.co.webee.presentation.interestpesticide.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "관심 농약 등록 response")
public record InterestPesticideRegisterResponse(
        @Schema(description = "관심 농약 ID", example = "1")
        Long interestPesticideId
) {
    public static InterestPesticideRegisterResponse of(Long interestPesticideId) {
        return InterestPesticideRegisterResponse.builder()
                .interestPesticideId(interestPesticideId)
                .build();
    }
}
