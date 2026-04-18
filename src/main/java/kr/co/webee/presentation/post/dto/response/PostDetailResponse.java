package kr.co.webee.presentation.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.post.entity.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "게시글 상세 response")
public record PostDetailResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long postId,

        @Schema(description = "제목", example = "제목")
        String title,

        @Schema(description = "내용", example = "내용")
        String content,

        @Schema(description = "좋아요 수", example = "10")
        int likeCount,

        @Schema(description = "작성일시", example = "2026-04-13T12:00:00")
        LocalDateTime createdAt
) {
    public static PostDetailResponse from(Post post) {
        return PostDetailResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
