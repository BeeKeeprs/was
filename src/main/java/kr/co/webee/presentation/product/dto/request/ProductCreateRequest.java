package kr.co.webee.presentation.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.type.BeeType;

@Schema(description = "상품 생성 요청")
public record ProductCreateRequest(

        @Schema(description = "상품 이름", example = "국산 꿀벌", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "상품 가격", example = "39000", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer price,

        @Schema(description = "벌 종류", example = "HONEYBEE", requiredMode = Schema.RequiredMode.REQUIRED)
        BeeType beeType,

        @Schema(description = "상품 설명", example = "상세 정보, 관리 방법, 거래 형태(임대/구매), 거래 방법(온/오프라인), 국내/해외 여부", requiredMode = Schema.RequiredMode.REQUIRED)
        String content
) {
}
