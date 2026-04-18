package kr.co.webee.presentation.post.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.post.dto.request.PostCreateRequest;
import kr.co.webee.presentation.post.dto.request.PostUpdateRequest;
import kr.co.webee.presentation.post.dto.response.PostCreateResponse;
import kr.co.webee.presentation.post.dto.response.PostDetailResponse;
import kr.co.webee.presentation.post.dto.response.PostListResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Post", description = "게시글 API")
public interface PostApi {
    @Operation(
            summary = "게시글 목록 조회 (Slice 페이징)",
            description = "게시글 목록을 Slice 단위로 조회합니다. (전체 건수 없이 다음 페이지 여부만 제공) 기본값은 size=10, id 내림차순입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
    })
    Slice<PostListResponse> getAllPosts(
            @Parameter(description = "페이지 정보 (page, size, sort 등 쿼리 파라미터)")
            Pageable pageable
    );

    @Operation(
            summary = "게시글 단건 조회",
            description = "게시글 ID에 해당하는 게시글 상세를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 없음"),
    })
    PostDetailResponse getPostDetail(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId
    );

    @Operation(
            summary = "게시글 등록",
            description = "게시글을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 등록 성공"),
    })
    PostCreateResponse createPost(
            @Parameter(description = "등록할 게시글", required = true)
            @RequestBody @Valid PostCreateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "게시글 수정",
            description = "본인이 작성한 게시글의 제목·내용을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "403", description = "작성자가 아님"),
            @ApiResponse(responseCode = "404", description = "게시글 없음"),
    })
    void updatePost(
            @Parameter(description = "게시글 ID", example = "1", required = true)
            @PathVariable Long postId,

            @Parameter(description = "수정할 내용", required = true)
            @RequestBody @Valid PostUpdateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );
}
