package kr.co.webee.infrastructure.bee.recommendation.nongsaro.dto;

import kr.co.webee.domain.bee.type.BeeType;

import java.util.List;

public record NongsaroCropPollinationDetailResponse(
        List<BeeType> beeTypes,
        String content
) {
    public static NongsaroCropPollinationDetailResponse of(List<BeeType> beeType, String content) {
        return new NongsaroCropPollinationDetailResponse(beeType, content);
    }
}
