package kr.co.webee.presentation.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.type.PostCategory;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "게시글 등록 request")
public record PostCreateRequest(
        @Schema(description = "제목", example = "제목입니다")
        @NotBlank
        String title,

        @Schema(description = "내용", example = "내용입니다")
        @NotBlank
        String content,

        @Schema(description = "카테고리 (KNOWHOW, QUESTION, NEWS, MARKET)", example = "KNOWHOW")
        @NotNull
        PostCategory category
) {
    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .category(category)
                .build();
    }
}
