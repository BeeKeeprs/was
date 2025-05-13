package kr.co.webee.presentation.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "리뷰 수정 요청")
public record ProductReviewUpdateRequest(
        @Schema(description = "수정할 리뷰 내용", example = "내용을 업데이트합니다.", requiredMode = REQUIRED)
        @NotBlank
        String content
) {
}
