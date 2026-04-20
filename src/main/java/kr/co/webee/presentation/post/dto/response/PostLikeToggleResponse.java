package kr.co.webee.presentation.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "게시글 좋아요 토글 response")
public record PostLikeToggleResponse(
        @Schema(description = "좋아요 상태", example = "true")
        boolean liked,

        @Schema(description = "좋아요 수", example = "11")
        int likeCount
) {
    public static PostLikeToggleResponse of(boolean liked, int likeCount) {
        return PostLikeToggleResponse.builder()
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }
}
