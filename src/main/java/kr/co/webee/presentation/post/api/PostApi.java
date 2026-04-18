package kr.co.webee.presentation.post.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.post.dto.request.PostCreateRequest;
import kr.co.webee.presentation.post.dto.response.PostCreateResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Post", description = "게시글 API")
public interface PostApi {
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
}
