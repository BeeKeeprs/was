package kr.co.webee.presentation.post.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.post.service.PostService;
import kr.co.webee.presentation.post.api.PostApi;
import kr.co.webee.presentation.post.dto.request.PostCreateRequest;
import kr.co.webee.presentation.post.dto.response.PostCreateResponse;
import kr.co.webee.presentation.post.dto.response.PostListResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController implements PostApi {
    private final PostService postService;

    @Override
    @GetMapping("")
    public Slice<PostListResponse> getAllPosts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @Override
    @PostMapping("")
    public PostCreateResponse createPost(@RequestBody @Valid PostCreateRequest request, @UserId Long userId) {
        return postService.createPost(request, userId);
    }
}
