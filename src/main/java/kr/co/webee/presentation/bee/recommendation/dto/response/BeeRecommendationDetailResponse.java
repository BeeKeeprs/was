package kr.co.webee.presentation.bee.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.recommendation.entity.BeeRecommendation;
import kr.co.webee.domain.bee.type.BeeType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "수정벌 추천 내역 목록 response")
public record BeeRecommendationDetailResponse(
        @Schema(description = "수정벌 추천 내역 ID", example = "1")
        Long beeRecommendationId,

        @Schema(description = "수정벌 종류", example = "EUROPEAN_BUMBLEBEE")
        BeeType beeType,

        @Schema(description = "수정벌 투입 적정 시작일", example = "2025-03-10")
        LocalDate inputStartDate,

        @Schema(description = "수정벌 투입 적정 마감일", example = "2025-04-08")
        LocalDate inputEndDate,

        @Schema(
                description = "수정벌 특징",
                example = "서양뒤영벌은 온실 환경에서의 우수한 활동성\n " +
                        "추운 날씨에도 활동 가능한 장점\n " +
                        "효율적인 꽃가루 전달 능력"

        )
        String characteristics,

        @Schema(
                description = "주의사항",
                example = "서양뒤영벌은 높은 온도와 습도에 민감한 점\n" +
                        "과도한 농약 사용은 서양뒤영벌의 활동에 부정적 영향"
        )
        String caution,

        @Schema(
                description = "사용 팁",
                example = "서양 뒤영벌을 시설 내에 배치할 때는 통풍이 잘 되는 지역에 두세요\n" +
                        "벌통을 햇빛이 직접 닿지 않는 곳에 배치하여 벌의 스트레스를 줄여주세요.\n" +
                        "수분이 잘 이루어지도록 하루에 한 번씩 시설 내 온도와 습도를 체크하세요."
        )
        String usageTip
) {
    public static BeeRecommendationDetailResponse from(BeeRecommendation beeRecommendation) {
        return BeeRecommendationDetailResponse.builder()
                .beeRecommendationId(beeRecommendation.getId())
                .beeType(beeRecommendation.getBeeType())
                .characteristics(beeRecommendation.getCharacteristics())
                .inputStartDate(beeRecommendation.getInputStartDate())
                .inputEndDate(beeRecommendation.getInputEndDate())
                .caution(beeRecommendation.getCaution())
                .usageTip(beeRecommendation.getUsageTip())
                .build();
    }
}
