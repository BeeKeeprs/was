package kr.co.webee.presentation.bee.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "수정벌 추천 ai 응답")
public record BeeRecommendationAiResponse(
        @Schema(description = "수정벌 종류", example = "뒤영벌")
        String beeType,

        @Schema(
                description = "수정벌 특징",
                example = "[\"뒤영벌은 온실 환경에서의 우수한 활동성\", " +
                        "\"추운 날씨에도 활동 가능한 장점\", " +
                        "\"효율적인 꽃가루 전달 능력\"]"
        )
        List<String> characteristics,

        @Schema(description = "수정벌 투입 적정 시작일", example = "2025-03-10")
        LocalDate inputStartDate,

        @Schema(description = "수정벌 투입 적정 마감일", example = "2025-04-08")
        LocalDate inputEndDate,

        @Schema(
                description = "주의사항",
                example = "[\"뒤영벌은 높은 온도와 습도에 민감한 점\", " +
                        "\"과도한 농약 사용은 뒤영벌의 활동에 부정적 영향\"]"
        )
        List<String> caution,

        @Schema(
                description = "사용 팁",
                example = "[\"뒤영벌을 시설 내에 배치할 때는 통풍이 잘 되는 지역에 두세요.\", " +
                        "\"벌통을 햇빛이 직접 닿지 않는 곳에 배치하여 벌의 스트레스를 줄여주세요.\", " +
                        "\"수분이 잘 이루어지도록 하루에 한 번씩 시설 내 온도와 습도를 체크하세요.\"]"
        )
        List<String> usageTip
) {
}
