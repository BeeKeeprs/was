package kr.co.webee.presentation.post.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.post.dto.response.PostLikeToggleResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Post Like", description = "게시글 좋아요 API")
public interface PostLikeApi {

    @Operation(
            summary = "게시글 좋아요 토글",
            description = "좋아요가 없으면 생성하고, 있으면 취소합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토글 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostLikeToggleResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.POST_NOT_FOUND)
    PostLikeToggleResponse toggleLike(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId,

            @Parameter(hidden = true)
            @UserId Long userId
    );
}
