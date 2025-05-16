package kr.co.webee.presentation.ai.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문서 벡터 요청 DTO")
public record VectorDocumentRequest(

        @Schema(description = "문서 본문", example = "이것은 문서입니다.")
        String content,

        @Schema(description = "카테고리 (예: bee, crop, guide 등)", example = "bee")
        String category,

        @Schema(description = "문서 타입 (예: faq, guide, tip 등)", example = "faq")
        String type,

        @Schema(description = "문서 출처 (manual, api, file 등)", example = "manual")
        String origin,

        @Schema(description = "신뢰도 (0.0 ~ 1.0)", example = "0.85")
        Double confidence

) {
}
