package kr.co.webee.presentation.post.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.post.service.PostCommentService;
import kr.co.webee.presentation.post.api.PostCommentApi;
import kr.co.webee.presentation.post.dto.request.PostCommentCreateRequest;
import kr.co.webee.presentation.post.dto.request.PostCommentUpdateRequest;
import kr.co.webee.presentation.post.dto.response.PostCommentCreateResponse;
import kr.co.webee.presentation.post.dto.response.PostCommentListResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostCommentController implements PostCommentApi {
    private final PostCommentService postCommentService;

    @Override
    @GetMapping("/{postId}/comments")
    public List<PostCommentListResponse> getAllComments(@PathVariable Long postId) {
        return postCommentService.getAllComments(postId);
    }

    @Override
    @PostMapping("/{postId}/comments")
    public PostCommentCreateResponse createComment(
            @PathVariable Long postId,
            @RequestBody @Valid PostCommentCreateRequest request,
            @UserId Long userId
    ) {
        return postCommentService.createComment(postId, request, userId);
    }

    @Override
    @PutMapping("/{postId}/comments/{commentId}")
    public void updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid PostCommentUpdateRequest request,
            @UserId Long userId
    ) {
        postCommentService.updateComment(postId, commentId, request, userId);
    }
}
