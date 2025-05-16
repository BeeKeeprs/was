package kr.co.webee.presentation.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "벡터 문서 응답 DTO")
public record VectorDocumentResponse(
        @Schema(description = "문서 내용", example = "벌통은 남향으로 배치하고, 바람을 막을 수 있는 위치가 이상적입니다.")
        String content,

        @Schema(description = "카테고리", example = "bee")
        String category,

        @Schema(description = "문서 타입", example = "guide")
        String type,

        @Schema(description = "생성자 ID", example = "1")
        String createdBy,

        @Schema(description = "문서 출처", example = "manual")
        String origin,

        @Schema(description = "문서 신뢰도 (0.0 ~ 1.0)", example = "0.95")
        Double confidence,

        @Schema(description = "생성 일시 (ISO-8601)", example = "2025-05-15T20:45:34.415799")
        String createdAt
) {
    @Builder
    public VectorDocumentResponse {}
}
