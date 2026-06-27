package kr.co.webee.presentation.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.post.type.PostCategory;
import lombok.Builder;

@Builder
@Schema(description = "게시글 수정 request")
public record PostUpdateRequest(
        @Schema(description = "제목", example = "수정된 제목")
        @NotBlank
        String title,

        @Schema(description = "내용", example = "수정된 내용")
        @NotBlank
        String content,

        @Schema(description = "카테고리 (KNOWHOW, QUESTION, NEWS, MARKET)", example = "KNOWHOW")
        @NotNull
        PostCategory category
) {
}
