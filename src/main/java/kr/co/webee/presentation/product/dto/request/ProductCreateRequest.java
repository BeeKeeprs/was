package kr.co.webee.presentation.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.product.enums.Origin;
import kr.co.webee.domain.product.enums.TransactionMethod;
import kr.co.webee.domain.product.enums.TransactionType;

@Schema(description = "상품 생성 요청")
public record ProductCreateRequest(
        @Schema(description = "업체 ID", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
        Long businessId,

        @Schema(description = "상품 이름", example = "국산 꿀벌", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "상품 가격", example = "39000", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer price,

        @Schema(description = "벌 종류", example = "HONEYBEE", requiredMode = Schema.RequiredMode.REQUIRED)
        BeeType beeType,

        @Schema(description = "상품 설명", example = "이 상품은 국내산 꿀벌입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
        String content,

        @Schema(description = "원산지", example = "국내산", requiredMode = Schema.RequiredMode.REQUIRED)
        Origin origin,

        @Schema(description = "거래 형태", example = "구매", requiredMode = Schema.RequiredMode.REQUIRED)
        TransactionType transactionType,

        @Schema(description = "거래 방법", example = "온라인", requiredMode = Schema.RequiredMode.REQUIRED)
        TransactionMethod transactionMethod
) {
}
