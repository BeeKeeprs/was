package kr.co.webee.presentation.bee.recommendation.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.bee.recommendation.entity.BeeRecommendation;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import kr.co.webee.domain.user.entity.User;

import java.time.LocalDate;

@Schema(description = "수정벌 추천 내역 저장 request")
public record BeeRecommendationRequest(
        @Schema(description = "작물명", example = "딸기")
        @NotBlank
        String cropName,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리")
        @NotBlank
        String cultivationAddress,

        @Schema(description = "재배 방식", example = "CONTROLLED", examples = {"CONTROLLED", "OPEN_FIELD"})
        @NotNull
        CultivationType cultivationType,

        @Schema(description = "수정벌 종류", example = "BUMBLEBEE")
        @NotBlank
        BeeType beeType,

        @Schema(
                description = "수정벌 특징",
                example = "뒤영벌은 온실 환경에서의 우수한 활동성\n " +
                        "추운 날씨에도 활동 가능한 장점\n " +
                        "효율적인 꽃가루 전달 능력"

        )
        @NotBlank
        String characteristics,

        @Schema(description = "수정벌 투입 적정 시작일", example = "2025-03-10")
        @NotBlank
        LocalDate inputStartDate,

        @Schema(description = "수정벌 투입 적정 마감일", example = "2025-04-08")
        @NotBlank
        LocalDate inputEndDate,

        @Schema(
                description = "주의사항",
                example = "뒤영벌은 높은 온도와 습도에 민감한 점\n" +
                        "과도한 농약 사용은 뒤영벌의 활동에 부정적 영향"
        )
        @NotBlank
        String caution,

        @Schema(
                description = "사용 팁",
                example = "뒤영벌을 시설 내에 배치할 때는 통풍이 잘 되는 지역에 두세요\n" +
                        "벌통을 햇빛이 직접 닿지 않는 곳에 배치하여 벌의 스트레스를 줄여주세요.\n" +
                        "수분이 잘 이루어지도록 하루에 한 번씩 시설 내 온도와 습도를 체크하세요."
        )
        @NotBlank
        String usageTip
) {
    public BeeRecommendation toEntity(User user) {
        return BeeRecommendation.builder()
                .cropName(cropName)
                .cultivationAddress(cultivationAddress)
                .cultivationType(cultivationType)
                .beeType(beeType)
                .characteristics(characteristics)
                .inputStartDate(inputStartDate)
                .inputEndDate(inputEndDate)
                .caution(caution)
                .usageTip(usageTip)
                .user(user)
                .build();
    }
}
