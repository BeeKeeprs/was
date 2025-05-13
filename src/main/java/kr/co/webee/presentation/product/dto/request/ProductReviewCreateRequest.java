package kr.co.webee.presentation.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "리뷰 생성 요청")
public record ProductReviewCreateRequest(
        @Schema(description = "리뷰 내용", example = "정말 만족스러워요!", requiredMode = REQUIRED)
        @NotBlank
        String content
) {
}
