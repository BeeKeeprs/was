package kr.co.webee.presentation.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.product.entity.ProductReview;

@Schema(description = "상품 리뷰 응답")
public record ProductReviewResponse(
        @Schema(description = "리뷰 ID", example = "101")
        Long id,

        @Schema(description = "리뷰 내용", example = "상품이 좋았어요!")
        String content,

        @Schema(description = "상품 ID", example = "5")
        Long productId,

        @Schema(description = "작성자 정보")
        WriterInfo writer
) {

    public static ProductReviewResponse from(ProductReview review) {
        return new ProductReviewResponse(
                review.getId(),
                review.getContent(),
                review.getProduct().getId(),
                new WriterInfo(review.getUser().getId(), review.getUser().getUsername())
        );
    }

    @Schema(description = "작성자 정보")
    public record WriterInfo(
            @Schema(description = "작성자 ID", example = "42")
            Long id,

            @Schema(description = "작성자 닉네임", example = "bee_master")
            String nickname
    ) {}
}
