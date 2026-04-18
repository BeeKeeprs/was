package kr.co.webee.presentation.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "게시글 등록 응답")
public record PostCreateResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long postId
) {
    public static PostCreateResponse of(Long postId) {
        return PostCreateResponse.builder()
                .postId(postId)
                .build();
    }
}
