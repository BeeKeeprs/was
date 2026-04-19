package kr.co.webee.presentation.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "게시글 댓글 등록 response")
public record PostCommentCreateResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId
) {
    public static PostCommentCreateResponse of(Long commentId) {
        return PostCommentCreateResponse.builder()
                .commentId(commentId)
                .build();
    }
}
