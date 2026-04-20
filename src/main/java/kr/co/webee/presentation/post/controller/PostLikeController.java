package kr.co.webee.presentation.post.controller;

import kr.co.webee.application.post.service.PostLikeService;
import kr.co.webee.presentation.post.api.PostLikeApi;
import kr.co.webee.presentation.post.dto.response.PostLikeStatusResponse;
import kr.co.webee.presentation.post.dto.response.PostLikeToggleResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostLikeController implements PostLikeApi {
    private final PostLikeService postLikeService;

    @Override
    @PostMapping("/{postId}/likes")
    public PostLikeToggleResponse toggleLike(
            @PathVariable Long postId,
            @UserId Long userId
    ) {
        return postLikeService.toggleLike(postId, userId);
    }

    @Override
    @GetMapping("/{postId}/likes/me")
    public PostLikeStatusResponse getMyLikeStatus(
            @PathVariable Long postId,
            @UserId Long userId
    ) {
        return postLikeService.getMyLikeStatus(postId, userId);
    }
}