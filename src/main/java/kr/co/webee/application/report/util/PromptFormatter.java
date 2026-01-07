package kr.co.webee.application.report.util;

import kr.co.webee.presentation.report.dto.HarvestPrediction;
import org.springframework.stereotype.Component;

@Component
public class PromptFormatter {
    public static String toPromptString(HarvestPrediction.Request request) {
        StringBuilder sb = new StringBuilder();

        sb.append("[사용자 입력 정보]\n\n");

        appendFarmBaseInfo(sb, request.farmBaseInfo());

        if (request.greenhouseSetup() != null) {
            appendGreenhouseSetup(sb, request.greenhouseSetup());
        }

        return sb.toString();
    }

    private static void appendFarmBaseInfo(StringBuilder sb, HarvestPrediction.FarmBaseInfo info) {
        sb.append("지역: ").append(info.region()).append("\n");
        sb.append("농지 면적: ").append(info.areaPyeong()).append("평\n");
        sb.append("재배 작물: ").append(info.cropName());

        if (info.cropVariety() != null && !info.cropVariety().isBlank()) {
            sb.append(" (품종: ").append(info.cropVariety()).append(")");
        }
        sb.append("\n\n");
    }

    private static void appendGreenhouseSetup(StringBuilder sb, HarvestPrediction.GreenhouseSetup setup) {
        HarvestPrediction.GreenhouseInfo g = setup.greenhouseInfo();
        HarvestPrediction.BeeUsageInfo b = setup.beeUsageInfo();
        HarvestPrediction.RevenueInfo r = setup.revenueInfo();

        sb.append("하우스 정보:\n");
        sb.append("- 하우스 수: ").append(g.houseCount()).append("동\n");
        sb.append("- 동당 면적: ").append(g.areaPerHousePyeong()).append("평\n");
        sb.append("- 사용 벌: ").append(b.beeType()).append("\n");
        sb.append("- 동당 벌 사용량: ").append(b.boxesPerHouse()).append("박스\n");
        sb.append("- 벌 교체 주기: ").append(b.replacementCycleWeeks()).append("주\n\n");

        sb.append("수익 정보:\n");
        appendRevenue(sb, "월간", r.monthly());
        appendRevenue(sb, "연간", r.yearly());

        if (setup.smartFarmInfo() != null) {
            appendSmartFarmInfo(sb, setup.smartFarmInfo());
        }
    }

    private static void appendRevenue(StringBuilder sb, String label, HarvestPrediction.Revenue revenue) {
        sb.append("- ").append(label).append(" 수익: ")
                .append(revenue.amountManWon()).append("만원 / ")
                .append(revenue.yieldKg()).append("kg\n");
    }

    private static void appendSmartFarmInfo(StringBuilder sb, HarvestPrediction.SmartFarmInfo info) {
        sb.append("\n스마트팜 관리:\n");
        sb.append("- 스마트팜 관리 여부: ")
                .append(info.isSmartFarmManaged() ? "예" : "아니오").append("\n");
        sb.append("- 온습도 관리 여부: ")
                .append(info.isTempHumidityManaged() ? "예" : "아니오").append("\n");

        if (info.isTempHumidityManaged()) {
            sb.append("- 평균 온도: ").append(info.avgTemperature()).append("도\n");
            sb.append("- 평균 습도: ").append(info.avgHumidity()).append("%\n");
        }
    }
}
