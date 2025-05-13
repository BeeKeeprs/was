package kr.co.webee.presentation.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.type.BeeType;

@Schema(description = "상품 수정 요청")
public record ProductUpdateRequest(

        @Schema(description = "상품 이름", example = "자연산 꿀벌", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "상품 가격", example = "15000", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer price,

        @Schema(description = "벌 종류", example = "HONEY", requiredMode = Schema.RequiredMode.REQUIRED)
        BeeType beeType,

        @Schema(description = "상품 설명", example = "자연산 순수 꿀입니다", requiredMode = Schema.RequiredMode.REQUIRED)
        String content
) {}
