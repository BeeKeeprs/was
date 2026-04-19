package kr.co.webee.presentation.post.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.post.dto.request.CommentCreateRequest;
import kr.co.webee.presentation.post.dto.response.CommentCreateResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Comment", description = "댓글 API")
public interface CommentApi {
    @Operation(
            summary = "댓글 등록",
            description = "특정 게시글에 댓글을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 등록 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 없음"),
    })
    CommentCreateResponse createComment(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId,

            @Parameter(description = "등록할 댓글", required = true)
            @RequestBody @Valid CommentCreateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );
}
