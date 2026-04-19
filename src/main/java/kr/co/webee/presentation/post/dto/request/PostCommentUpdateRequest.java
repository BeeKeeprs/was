package kr.co.webee.presentation.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "댓글 수정 request")
public record PostCommentUpdateRequest(
        @Schema(description = "댓글 내용", example = "수정된 댓글")
        @NotBlank
        String content
) {
}
