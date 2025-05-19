package kr.co.webee.presentation.bee.recommend.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.recommend.entity.BeeRecommendation;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.profile.crop.entity.UserCrop;

import java.time.LocalDate;

@Schema(description = "수정벌 추천 내역 저장 request")
public record BeeRecommendationRequest(
        @Schema(description = "수정벌 종류", example = "EUROPEAN_BUMBLEBEE")
        BeeType beeType,

        @Schema(
                description = "수정벌 특징",
                example = "서양뒤영벌은 온실 환경에서의 우수한 활동성\n " +
                        "추운 날씨에도 활동 가능한 장점\n " +
                        "효율적인 꽃가루 전달 능력"

        )
        String characteristics,

        @Schema(description = "수정벌 투입 적정 시작일", example = "2025-03-10")
        LocalDate inputStartDate,

        @Schema(description = "수정벌 투입 적정 마감일", example = "2025-04-08")
        LocalDate inputEndDate,

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
        String usageTip,

        @Schema(description = "사용자 재배 작물 ID", example = "1")
        Long userCropId
) {
    public BeeRecommendation toEntity(UserCrop userCrop) {
        return BeeRecommendation.builder()
                .beeType(beeType)
                .characteristics(characteristics)
                .inputStartDate(inputStartDate)
                .inputEndDate(inputEndDate)
                .caution(caution)
                .usageTip(usageTip)
                .userCrop(userCrop)
                .build();
    }
}
