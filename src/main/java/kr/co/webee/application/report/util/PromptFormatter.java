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

        if (request.environmentData() != null) {
            appendEnvironmentData(sb, request.environmentData());
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
        sb.append("하우스 시설 정보:\n");
        sb.append("- 하우스 수: ").append(setup.houseCount()).append("동\n");
        sb.append("- 동당 면적: ").append(setup.areaPerHousePyeong()).append("평\n");
        sb.append("- 사용 벌: ").append(setup.beeType()).append("\n");
        sb.append("- 동당 벌 사용량: ").append(setup.boxesPerHouse()).append("박스\n");
        sb.append("- 벌 교체 주기: ").append(setup.replacementCycleWeeks()).append("주\n");
        sb.append("- 연 생산량: ").append(setup.annualYieldKg()).append("kg\n\n");
    }

    private static void appendEnvironmentData(StringBuilder sb, HarvestPrediction.EnvironmentData data) {
        sb.append("환경 데이터:\n");
        if (data.temperature() != null) {
            sb.append("- 온도: ").append(data.temperature()).append("°C\n");
        }
        if (data.humidity() != null) {
            sb.append("- 습도: ").append(data.humidity()).append("%\n");
        }
    }
}
