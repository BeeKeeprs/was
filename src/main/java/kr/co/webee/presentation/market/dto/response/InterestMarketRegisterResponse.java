package kr.co.webee.presentation.market.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "관심 시장 등록 response")
public record InterestMarketRegisterResponse(
        @Schema(description = "관심 시장 ID", example = "1")
        Long interestMarketId
) {
    public static InterestMarketRegisterResponse of(Long interestMarketId) {
        return InterestMarketRegisterResponse.builder()
                .interestMarketId(interestMarketId)
                .build();
    }
}

