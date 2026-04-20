package kr.co.webee.presentation.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "내 게시글 좋아요 상태 response")
public record PostLikeStatusResponse(
        @Schema(description = "좋아요 여부", example = "true")
        boolean liked
) {
    public static PostLikeStatusResponse of(boolean liked) {
        return PostLikeStatusResponse.builder()
                .liked(liked)
                .build();
    }
}
