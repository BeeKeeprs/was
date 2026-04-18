package kr.co.webee.presentation.post.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.post.service.PostService;
import kr.co.webee.presentation.post.api.PostApi;
import kr.co.webee.presentation.post.dto.request.PostCreateRequest;
import kr.co.webee.presentation.post.dto.response.PostCreateResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController implements PostApi {
    private final PostService postService;

    @Override
    @PostMapping("")
    public PostCreateResponse createPost(@RequestBody @Valid PostCreateRequest request, @UserId Long userId) {
        return postService.createPost(request, userId);
    }
}
