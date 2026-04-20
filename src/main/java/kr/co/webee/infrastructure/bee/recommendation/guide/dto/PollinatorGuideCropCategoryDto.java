package kr.co.webee.infrastructure.bee.recommendation.guide.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "수정벌 추천 카테고리 및 작물명")
public record PollinatorGuideCropCategoryDto(
        @Schema(description = "작물 대분류", example = "과수")
        String category,

        @Schema(description = "해당 카테고리에 속하는 작물 표시명 목록", example = "[\"시설복숭아\", \"복숭아\", \"사과\"]")
        List<String> crops
) {
    public static PollinatorGuideCropCategoryDto of(String category, List<String> crops) {
        return PollinatorGuideCropCategoryDto.builder()
                .category(category)
                .crops(crops)
                .build();
    }
}
