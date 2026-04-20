package kr.co.webee.infrastructure.bee.recommendation.guide.dto;

import java.util.List;

public record PollinatorGuideDto(
        String version,
        String source,
        String sourceUrl,
        String lastUpdated,
        List<Crop> crops
) {

    public record Crop(
            String name,
            String category,
            String menuId,
            String cntntsNo,
            List<String> applicableVarieties,
            String usagePeriod,
            List<Pollinator> pollinators,
            Precautions precautions,
            Effectiveness effectiveness
    ) {
    }

    public record Pollinator(
            String insectType,
            int durationDays,
            String inputTiming,
            String releaseQuantity,
            List<String> installationSteps
    ) {
    }

    public record Precautions(
            List<String> colonyManagement,
            List<String> temperature,
            List<String> pesticideSafety
    ) {
    }

    public record Effectiveness(
            String source,
            List<String> highlights
    ) {
    }
}
