package kr.co.webee.presentation.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.post.entity.Comment;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "댓글 등록 request")
public record CommentCreateRequest(
        @Schema(description = "댓글 내용", example = "댓글입니다")
        @NotBlank
        String content
) {
    public Comment toEntity(Post post, User user) {
        return Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
    }
}
