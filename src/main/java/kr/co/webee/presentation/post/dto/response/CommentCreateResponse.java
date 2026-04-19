package kr.co.webee.presentation.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "댓글 등록 response")
public record CommentCreateResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId
) {
    public static CommentCreateResponse of(Long commentId) {
        return CommentCreateResponse.builder()
                .commentId(commentId)
                .build();
    }
}
