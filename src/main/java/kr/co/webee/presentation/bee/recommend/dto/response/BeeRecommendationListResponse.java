package kr.co.webee.presentation.bee.recommend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.recommend.entity.BeeRecommendation;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Schema(description = "수정벌 추천 내역 목록 response")
public record BeeRecommendationListResponse(
        @Schema(description = "수정벌 추천 내역 ID", example = "1")
        Long beeRecommendationId,

        @Schema(description = "수정벌 종류", example = "EUROPEAN_BUMBLEBEE")
        BeeType beeType,

        @Schema(description = "수정벌 투입 적정 시작일", example = "2025-03-10")
        LocalDate inputStartDate,

        @Schema(description = "수정벌 투입 적정 마감일", example = "2025-04-08")
        LocalDate inputEndDate,

        @Schema(description = "작물명", example = "블루베리")
        String cropName,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리")
        String cultivationAddress,

        @Schema(description = "재배 형태", example = "OPEN_FIELD")
        CultivationType cultivationType,

        @Schema(description = "추천일", example = "2025-04-01")
        LocalDate createdAt

) {
    public static BeeRecommendationListResponse from(BeeRecommendation beeRecommendation) {
        return BeeRecommendationListResponse.builder()
                .beeRecommendationId(beeRecommendation.getId())
                .beeType(beeRecommendation.getBeeType())
                .inputStartDate(beeRecommendation.getInputStartDate())
                .inputEndDate(beeRecommendation.getInputEndDate())
                .cropName(beeRecommendation.getUserCrop().getName())
                .cultivationAddress(beeRecommendation.getUserCrop().getCultivationAddress())
                .cultivationType(beeRecommendation.getUserCrop().getCultivationType())
                .createdAt(LocalDate.from(beeRecommendation.getCreatedAt()))
                .build();
    }
}
