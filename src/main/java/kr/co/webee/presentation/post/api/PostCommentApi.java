package kr.co.webee.presentation.post.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.post.dto.request.PostCommentCreateRequest;
import kr.co.webee.presentation.post.dto.request.PostCommentUpdateRequest;
import kr.co.webee.presentation.post.dto.response.PostCommentCreateResponse;
import kr.co.webee.presentation.post.dto.response.PostCommentListResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Post Comment", description = "게시글 댓글 API")
public interface PostCommentApi {

    @Operation(
            summary = "댓글 목록 조회",
            description = "특정 게시글의 댓글 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostCommentListResponse.class))
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.POST_NOT_FOUND)
    List<PostCommentListResponse> getAllComments(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId
    );

    @Operation(
            summary = "댓글 등록",
            description = "특정 게시글에 댓글을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentCreateResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.POST_NOT_FOUND)
    PostCommentCreateResponse createComment(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId,

            @Parameter(
                    description = "등록할 댓글",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentCreateRequest.class)
                    )
            )
            @RequestBody @Valid PostCommentCreateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "댓글 수정",
            description = "특정 게시글에 달린 본인 댓글의 내용을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
    })
    @ApiDocsErrorType({
            ErrorType.POST_COMMENT_NOT_FOUND,
            ErrorType.POST_COMMENT_ACCESS_DENIED,
    })
    void updateComment(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId,

            @Parameter(description = "댓글 ID", example = "1", required = true)
            @PathVariable Long commentId,

            @Parameter(
                    description = "수정할 내용",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentUpdateRequest.class)
                    )
            )
            @RequestBody @Valid PostCommentUpdateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );
}
