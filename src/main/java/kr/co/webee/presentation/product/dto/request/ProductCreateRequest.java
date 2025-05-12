package kr.co.webee.presentation.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.type.BeeType;

@Schema(description = "상품 생성 요청")
public record ProductCreateRequest(

        @Schema(description = "상품 가격", example = "15000", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer price,

        @Schema(description = "벌 종류", example = "HONEYBEE", requiredMode = Schema.RequiredMode.REQUIRED)
        BeeType beeType,

        @Schema(description = "상품 설명", example = "국산 꿀벌입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
        String content
) {
}
