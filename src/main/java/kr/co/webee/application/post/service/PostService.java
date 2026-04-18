package kr.co.webee.application.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.repository.PostRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.post.dto.request.PostCreateRequest;
import kr.co.webee.presentation.post.dto.request.PostUpdateRequest;
import kr.co.webee.presentation.post.dto.response.PostCreateResponse;
import kr.co.webee.presentation.post.dto.response.PostDetailResponse;
import kr.co.webee.presentation.post.dto.response.PostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Slice<PostListResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostListResponse::from);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_NOT_FOUND));

        return PostDetailResponse.from(post);
    }

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.save(request.toEntity(user));

        return PostCreateResponse.of(post.getId());
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_NOT_FOUND));

        if (post.isNotWrittenBy(userId)) {
            throw new BusinessException(ErrorType.POST_ACCESS_DENIED);
        }

        post.update(request.title(), request.content());
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_NOT_FOUND));

        if (post.isNotWrittenBy(userId)) {
            throw new BusinessException(ErrorType.POST_ACCESS_DENIED);
        }

        postRepository.delete(post);
    }
}
