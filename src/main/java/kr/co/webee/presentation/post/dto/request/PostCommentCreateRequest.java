package kr.co.webee.presentation.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.entity.PostComment;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "게시글 댓글 등록 request")
public record PostCommentCreateRequest(
        @Schema(description = "댓글 내용", example = "댓글입니다")
        @NotBlank
        String content
) {
    public PostComment toEntity(Post post, User user) {
        return PostComment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
    }
}
