package kr.co.webee.presentation.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class HarvestPrediction {
    @Schema(description = "수확량 예측 요청")
    public record Request(
            @Schema(description = "농지 기본 정보")
            FarmBaseInfo farmBaseInfo,
            @Schema(description = "하우스 설정 정보")
            GreenhouseSetup greenhouseSetup
    ){}

    @Schema(description = "농지 기본 정보")
    public record FarmBaseInfo(
            @Schema(description = "지역", example = "논산")
            String region,
            @Schema(description = "농지 면적 (평)", example = "1000")
            int areaPyeong,
            @Schema(description = "재배 작물명", example = "딸기")
            String cropName,
            @Schema(description = "품종 (선택사항)", example = "설향", nullable = true)
            String cropVariety
    ) {}

    @Schema(description = "하우스 설정 정보")
    public record GreenhouseSetup(
            @Schema(description = "하우스 정보")
            GreenhouseInfo greenhouseInfo,
            @Schema(description = "벌 사용 정보")
            BeeUsageInfo beeUsageInfo,
            @Schema(description = "수익 정보")
            RevenueInfo revenueInfo,
            @Schema(description = "스마트팜 정보 (선택사항)", nullable = true)
            SmartFarmInfo smartFarmInfo
    ) {}

    @Schema(description = "하우스 정보")
    public record GreenhouseInfo(
            @Schema(description = "하우스 수 (동)", example = "3")
            int houseCount,
            @Schema(description = "동당 면적 (평)", example = "330")
            int areaPerHousePyeong
    ) {}

    @Schema(description = "벌 사용 정보")
    public record BeeUsageInfo(
            @Schema(description = "사용 벌 종류", example = "지리산 수정벌")
            String beeType,
            @Schema(description = "동당 벌 사용량 (박스)", example = "3")
            int boxesPerHouse,
            @Schema(description = "벌 교체 주기 (주)", example = "3")
            int replacementCycleWeeks
    ) {}

    @Schema(description = "수익 정보")
    public record RevenueInfo(
            @Schema(description = "월간 수익")
            Revenue monthly,
            @Schema(description = "연간 수익")
            Revenue yearly
    ) {}

    @Schema(description = "수익")
    public record Revenue(
            @Schema(description = "수익 금액 (만원)", example = "500")
            int amountManWon,
            @Schema(description = "수확량 (kg)", example = "1000")
            int yieldKg
    ) {}

    @Schema(description = "스마트팜 정보")
    public record SmartFarmInfo(
            @Schema(description = "스마트팜 관리 여부", example = "true")
            boolean isSmartFarmManaged,
            @Schema(description = "온습도 관리 여부", example = "true")
            boolean isTempHumidityManaged,
            @Schema(description = "평균 온도 (도)", example = "25.5", nullable = true)
            Double avgTemperature,
            @Schema(description = "평균 습도 (%)", example = "65.0", nullable = true)
            Double avgHumidity
    ) {}

    @Schema(description = "수확량 예측 AI 응답")
    public record Response(
            @Schema(
                    description = "고품질(특/상 등급) 작물 생산 가능성",
                    example = "높음",
                    allowableValues = {"높음", "보통", "낮음"}
            )
            String qualityPossibility,

            @Schema(
                    description = "상세 분석",
                    example = "개화 기간 내내 벌들의 수분 활동이 꾸준하고, 특히 화분 매개 활동 비율이 높아 정형과(모양이 예쁜 과일) 생산 비율이 높을 것으로 기대됩니다."
            )
            String detailedAnalysis,

            @Schema(
                    description = "수확량 예측",
                    example = "현재의 우수한 수분 활동이 유지될 경우, 과거 평균 데이터 대비 최대 15%의 수확량 증대 잠재력이 있습니다."
            )
            String yieldPrediction,

            @Schema(
                    description = "제안",
                    example = "고품질 작물 생산이 예상되오니, 수확기까지 꾸준한 양분 관리를 통해 과실의 크기를 키우는 데 집중하시는 것을 추천합니다."
            )
            String suggestion
    ) {}

}