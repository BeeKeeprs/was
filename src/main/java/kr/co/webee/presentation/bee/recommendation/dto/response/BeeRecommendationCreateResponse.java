package kr.co.webee.presentation.bee.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "수정벌 추천 내역 생성 response")
public record BeeRecommendationCreateResponse(
        @Schema(description = "수정벌 추천 기록 ID", example = "1")
        Long beeRecommendationId
) {
    public static BeeRecommendationCreateResponse of(Long beeRecommendationId) {
        return BeeRecommendationCreateResponse.builder()
                .beeRecommendationId(beeRecommendationId)
                .build();
    }
}
