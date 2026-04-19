package kr.co.webee.presentation.post.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.post.service.CommentService;
import kr.co.webee.presentation.post.api.CommentApi;
import kr.co.webee.presentation.post.dto.request.CommentCreateRequest;
import kr.co.webee.presentation.post.dto.response.CommentCreateResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class CommentController implements CommentApi {
    private final CommentService commentService;

    @Override
    @PostMapping("/{postId}/comments")
    public CommentCreateResponse createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequest request,
            @UserId Long userId
    ) {
        return commentService.createComment(postId, request, userId);
    }
}
