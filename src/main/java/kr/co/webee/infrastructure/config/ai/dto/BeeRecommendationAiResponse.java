package kr.co.webee.infrastructure.config.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "수정벌 추천 ai 응답")
public record BeeRecommendationAiResponse(
        @Schema(description = "수정벌 종류", example = "서양 뒤영벌")
        String beeType,

        @Schema(
                description = "수정벌 특징",
                example = "[\"서양 뒤영벌은 온도 변화에 강하며, 특히 저온에서도 활발하게 활동할 수 있어 시설 재배에 적합합니다.\", " +
                        "\"크기가 작아 딸기 꽃을 손쉽게 이동하며 수정할 수 있습니다.\", " +
                        "\"하루에 여러 번 같은 꽃을 방문할 수 있어 수정 효율이 높습니다.\"]"
        )
        List<String> characteristics,

        @Schema(description = "수정벌 투입 적정 시작일", example = "2025-03-10")
        LocalDate inputStartDate,

        @Schema(description = "수정벌 투입 적정 마감일", example = "2025-04-08")
        LocalDate inputEndDate,

        @Schema(
                description = "주의사항",
                example = "[\"서양 뒤영벌은 습도에 민감하므로, 시설 내 습도를 적절히 유지해야 합니다.\", " +
                        "\"농약 사용 시에는 벌의 활동에 영향을 줄 수 있으니 주의가 필요합니다.\"]"
        )
        List<String> caution,

        @Schema(
                description = "사용 팁",
                example = "[\"서양 뒤영벌을 시설 내에 배치할 때는 통풍이 잘 되는 지역에 두세요.\", " +
                        "\"벌통을 햇빛이 직접 닿지 않는 곳에 배치하여 벌의 스트레스를 줄여주세요.\", " +
                        "\"수분이 잘 이루어지도록 하루에 한 번씩 시설 내 온도와 습도를 체크하세요.\"]"
        )
        List<String> usageTip
) {
}
