package kr.co.webee.presentation.community.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.post.type.PostCategory;
import lombok.Builder;

@Builder
@Schema(description = "지금 뜨는 주제 정보")
public record TrendingCategoryResponse(

        @Schema(description = "카테고리", example = "KNOWHOW")
        PostCategory category,

        @Schema(description = "게시글 수", example = "42")
        long postCount
) {
    public static TrendingCategoryResponse of(PostCategory category, long postCount) {
        return TrendingCategoryResponse.builder()
                .category(category)
                .postCount(postCount)
                .build();
    }
}
