package kr.co.webee.presentation.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.post.entity.PostComment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "게시글 댓글 목록 response")
public record PostCommentListResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,

        @Schema(description = "댓글 내용", example = "댓글입니다")
        String content,

        @Schema(description = "작성자 이름", example = "홍길동")
        String name,

        @Schema(description = "작성 일시", example = "2025-05-15T20:45:34.415799")
        LocalDateTime createdAt
) {
    public static PostCommentListResponse from(PostComment postComment) {
        return PostCommentListResponse.builder()
                .commentId(postComment.getId())
                .content(postComment.getContent())
                .name(postComment.getUser().getName())
                .createdAt(postComment.getCreatedAt())
                .build();
    }
}