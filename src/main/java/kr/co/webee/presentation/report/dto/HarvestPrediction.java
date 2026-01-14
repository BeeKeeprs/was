package kr.co.webee.presentation.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class HarvestPrediction {
    @Schema(description = "수확량 예측 요청")
    public record Request(
            @Schema(description = "농지 기본 정보")
            FarmBaseInfo farmBaseInfo,
            @Schema(description = "하우스 설정 정보")
            GreenhouseSetup greenhouseSetup,
            @Schema(description = "환경 데이터 (선택사항)", nullable = true)
            EnvironmentData environmentData
    ){}

    @Schema(description = "농지 기본 정보")
    public record FarmBaseInfo(
            @Schema(description = "지역", example = "에천")
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
            @Schema(description = "하우스 수 (동)", example = "3")
            int houseCount,
            @Schema(description = "동당 면적 (평)", example = "330")
            int areaPerHousePyeong,
            @Schema(description = "사용 벌 종류", example = "지리산 수정벌")
            String beeType,
            @Schema(description = "동당 벌 사용량 (박스)", example = "3")
            int boxesPerHouse,
            @Schema(description = "벌 교체 주기 (주)", example = "3")
            int replacementCycleWeeks,
            @Schema(description = "연 생산량 (kg)", example = "5000")
            int annualYieldKg
    ) {}

    @Schema(description = "환경 데이터")
    public record EnvironmentData(
            @Schema(description = "평균 온도 (°C)", example = "27")
            Integer temperature,
            @Schema(description = "평균 습도 (%)", example = "85")
            Integer humidity
    ) {}

    @Schema(description = "스마트 농업 분석 리포트 응답")
    public record Response(
            @Schema(description = "수정벌 관리 최적화 지수")
            SummaryScore summaryScore,

            @Schema(description = "맞춤 수정벌 추천")
            Recommendation recommendation,

            @Schema(description = "온습도 상태 진단")
            EnvironmentDiagnosis environmentDiagnosis,

            @Schema(description = "우선 개선 사항 (심각 순)")
            java.util.List<PriorityIssue> priorityIssues,

            @Schema(description = "관리 가이드")
            ManagementGuide managementGuide,

            @Schema(description = "기대 수익 분석")
            ExpectedRevenue expectedRevenue,

            @Schema(description = "최종 결론 요약")
            FinalConclusion finalConclusion
    ) {}

    @Schema(description = "수정벌 관리 최적화 지수")
    public record SummaryScore(
            @Schema(description = "점수 (0~100)", example = "85")
            int score,

            @Schema(description = "상위 퍼센트", example = "60")
            int percentile
    ) {}

    @Schema(description = "맞춤 수정벌 추천")
    public record Recommendation(
            @Schema(description = "최적 추천 벌")
            BeeRecommendationDetail best,

            @Schema(description = "대안 벌 목록")
            java.util.List<BeeRecommendationAlternative> alternatives
    ) {}

    @Schema(description = "수정벌 추천 상세 정보")
    public record BeeRecommendationDetail(
            @Schema(description = "벌 이름", example = "지리산 수정벌")
            String name,

            @Schema(description = "가격 (원)", example = "45000")
            int price,

            @Schema(description = "교체 주기 (주)", example = "3")
            int replacementCycleWeeks,

            @Schema(description = "활동률 (%)", example = "92")
            int activityRate,

            @Schema(description = "최적 온도 범위")
            TemperatureRange optimalTemperature,

            @Schema(description = "특징 태그", example = "[\"고활동성\", \"저온적응\"]")
            java.util.List<String> tags
    ) {}

    @Schema(description = "수정벌 대안 정보")
    public record BeeRecommendationAlternative(
            @Schema(description = "벌 이름", example = "호박벌")
            String name,

            @Schema(description = "가격 (원)", example = "38000")
            int price,

            @Schema(description = "교체 주기 (주)", example = "4")
            int replacementCycleWeeks,

            @Schema(description = "활동률 (%)", example = "88")
            int activityRate,

            @Schema(description = "특징 태그", example = "[\"경제적\", \"안정적\"]")
            java.util.List<String> tags
    ) {}

    @Schema(description = "온도 범위")
    public record TemperatureRange(
            @Schema(description = "최소 온도 (°C)", example = "15")
            int min,

            @Schema(description = "최대 온도 (°C)", example = "28")
            int max
    ) {}

    @Schema(description = "온습도 상태 진단")
    public record EnvironmentDiagnosis(
            @Schema(description = "온도 진단")
            TemperatureDiagnosis temperature,

            @Schema(description = "습도 진단")
            HumidityDiagnosis humidity,

            @Schema(description = "경고 메시지 목록")
            java.util.List<String> alerts
    ) {}

    @Schema(description = "온도 진단")
    public record TemperatureDiagnosis(
            @Schema(description = "현재 온도 (°C)", example = "27")
            int current,

            @Schema(description = "최적 온도 범위")
            TemperatureRange optimal,

            @Schema(description = "상태", example = "NORMAL", allowableValues = {"LOW", "NORMAL", "HIGH"})
            String status
    ) {}

    @Schema(description = "습도 진단")
    public record HumidityDiagnosis(
            @Schema(description = "현재 습도 (%)", example = "85")
            int current,

            @Schema(description = "최적 습도 범위")
            HumidityRange optimal,

            @Schema(description = "상태", example = "HIGH", allowableValues = {"LOW", "NORMAL", "HIGH"})
            String status
    ) {}

    @Schema(description = "습도 범위")
    public record HumidityRange(
            @Schema(description = "최소 습도 (%)", example = "60")
            int min,

            @Schema(description = "최대 습도 (%)", example = "80")
            int max
    ) {}

    @Schema(description = "우선 개선 사항")
    public record PriorityIssue(
            @Schema(description = "우선순위 순번", example = "1")
            int rank,

            @Schema(description = "제목", example = "습도 과다")
            String title,

            @Schema(description = "설명", example = "현재 습도가 85%로 최적 범위(60-80%)를 초과하여 병해 발생 위험이 있습니다.")
            String description
    ) {}

    @Schema(description = "관리 가이드")
    public record ManagementGuide(
            @Schema(description = "온도 관리")
            TemperatureControl temperatureControl,

            @Schema(description = "습도 관리")
            HumidityControl humidityControl,

            @Schema(description = "수분 관리")
            PollinationManagement pollinationManagement
    ) {}

    @Schema(description = "온도 관리")
    public record TemperatureControl(
            @Schema(description = "현재 범위", example = "27°C")
            String currentRange,

            @Schema(description = "권장 범위", example = "18-25°C")
            String recommendedRange,

            @Schema(description = "조치 사항")
            java.util.List<String> actions
    ) {}

    @Schema(description = "습도 관리")
    public record HumidityControl(
            @Schema(description = "현재 범위", example = "85%")
            String currentRange,

            @Schema(description = "권장 범위", example = "60-80%")
            String recommendedRange,

            @Schema(description = "조치 사항")
            java.util.List<String> actions
    ) {}

    @Schema(description = "수분 관리")
    public record PollinationManagement(
            @Schema(description = "벌통 배치", example = "하우스 중앙, 지면에서 50cm 높이")
            String placement,

            @Schema(description = "교체 주기", example = "3주마다 교체")
            String replacementCycle,

            @Schema(description = "추가 팁")
            java.util.List<String> additionalTips
    ) {}

    @Schema(description = "기대 수익 분석")
    public record ExpectedRevenue(
            @Schema(description = "시장 가격 (원/kg)", example = "8000")
            int marketPricePerKg,

            @Schema(description = "연간 생산량 (kg)", example = "5000")
            int annualProductionKg,

            @Schema(description = "현재 수분율 (%)", example = "75")
            int currentPollinationRate,

            @Schema(description = "개선 후 수분율 (%)", example = "92")
            int improvedPollinationRate,

            @Schema(description = "추가 생산량 (kg)", example = "850")
            int additionalProductionKg,

            @Schema(description = "추가 수익 (원)", example = "6800000")
            int additionalRevenue,

            @Schema(description = "추가 비용 범위")
            CostRange additionalCost,

            @Schema(description = "순이익 범위")
            RevenueRange netGainRange,

            @Schema(description = "ROI 범위 (%)")
            PercentageRange roiPercentRange
    ) {}

    @Schema(description = "비용 범위")
    public record CostRange(
            @Schema(description = "최소 비용 (원)", example = "500000")
            int min,

            @Schema(description = "최대 비용 (원)", example = "800000")
            int max
    ) {}

    @Schema(description = "수익 범위")
    public record RevenueRange(
            @Schema(description = "최소 수익 (원)", example = "6000000")
            int min,

            @Schema(description = "최대 수익 (원)", example = "6300000")
            int max
    ) {}

    @Schema(description = "퍼센트 범위")
    public record PercentageRange(
            @Schema(description = "최소 퍼센트", example = "750")
            int min,

            @Schema(description = "최대 퍼센트", example = "1260")
            int max
    ) {}

    @Schema(description = "최종 결론 요약")
    public record FinalConclusion(
            @Schema(description = "적합성 평가", example = "매우 적합")
            String suitability,

            @Schema(description = "기대 개선율 범위 (%)")
            PercentageRange expectedImprovementRate,

            @Schema(description = "기대 연간 수익 증가 범위 (원)")
            RevenueRange expectedAnnualRevenueIncrease
    ) {}

}