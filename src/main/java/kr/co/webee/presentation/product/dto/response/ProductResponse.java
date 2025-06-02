package kr.co.webee.presentation.product.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.bee.type.BeeType;
import kr.co.webee.domain.product.enums.Origin;
import kr.co.webee.domain.product.entity.Product;
import kr.co.webee.domain.product.enums.TransactionMethod;
import kr.co.webee.domain.product.enums.TransactionType;

import java.util.List;

@Schema(description = "상품 응답 DTO")
public record ProductResponse(

        @Schema(description = "상품 ID", example = "1")
        Long id,

        @Schema(description = "상품 이름", example = "국산 꿀벌")
        String name,

        @Schema(description = "상품 가격 (단위: 원)", example = "15000")
        Integer price,

        @Schema(description = "벌 종류", example = "HONEY")
        BeeType beeType,

        @Schema(description = "상품 설명", example = "이 상품은 국내산 꿀벌입니다.")
        String content,

        @Schema(description = "원산지", example = "국내산")
        Origin origin,

        @Schema(description = "거래 형태", example = "구매")
        TransactionType transactionType,

        @Schema(description = "거래 방법", example = "온라인")
        TransactionMethod transactionMethod,

        @Schema(description = "판매자 ID", example = "42")
        Long sellerId,

        @ArraySchema(schema = @Schema(description = "상품 이미지 URL", example = "https://cataas.com/cat?width=600"))
        List<String> imageUrls

) {
    public static ProductResponse from(Product product, List<String> imageUrls) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getBeeType(),
                product.getContent(),
                product.getOrigin(),
                product.getTransactionType(),
                product.getTransactionMethod(),
                product.getSeller().getId(),
                imageUrls
        );
    }
}
