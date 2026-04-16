package kr.co.webee.presentation.interestmarket.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.interestmarket.entity.InterestMarket;
import lombok.Builder;

@Builder
@Schema(description = "관심 시장 정보")
public record InterestMarketResponse(
        @Schema(description = "관심 시장 ID", example = "1")
        Long interestMarketId,

        @Schema(description = "시장 코드", example = "110001")
        String marketCode,

        @Schema(description = "작물 대분류 코드", example = "01", nullable = true)
        String cropMajorCode,

        @Schema(description = "작물 중분류 이름", example = "딸기", nullable = true)
        String cropMidName,

        @Schema(description = "작물 소분류 이름", example = "설향", nullable = true)
        String cropMinorName
) {
    public static InterestMarketResponse from(InterestMarket interestMarket) {
        return InterestMarketResponse.builder()
                .interestMarketId(interestMarket.getId())
                .marketCode(interestMarket.getMarketCode())
                .cropMajorCode(interestMarket.getCropMajorCode())
                .cropMidName(interestMarket.getCropMidName())
                .cropMinorName(interestMarket.getCropMinorName())
                .build();
    }
}

