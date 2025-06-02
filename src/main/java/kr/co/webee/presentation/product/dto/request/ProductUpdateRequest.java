package kr.co.webee.presentation.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.product.enums.Origin;
import kr.co.webee.domain.product.enums.TransactionMethod;
import kr.co.webee.domain.product.enums.TransactionType;

@Schema(description = "상품 수정 요청")
public record ProductUpdateRequest(

        @Schema(description = "상품 이름", example = "자연산 꿀벌", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "상품 가격 (단위: 원)", example = "15000", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer price,

        @Schema(description = "벌 종류", example = "HONEY", requiredMode = Schema.RequiredMode.REQUIRED)
        BeeType beeType,

        @Schema(description = "상품 설명", example = "국내산 꿀벌입니다", requiredMode = Schema.RequiredMode.REQUIRED)
        String content,

        @Schema(description = "원산지", example = "국내산", requiredMode = Schema.RequiredMode.REQUIRED)
        Origin origin,

        @Schema(description = "거래 형태", example = "임대", requiredMode = Schema.RequiredMode.REQUIRED)
        TransactionType transactionType,

        @Schema(description = "거래 방법", example = "오프라인", requiredMode = Schema.RequiredMode.REQUIRED)
        TransactionMethod transactionMethod

) {
}
