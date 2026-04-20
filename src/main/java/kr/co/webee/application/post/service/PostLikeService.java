package kr.co.webee.application.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.entity.PostLike;
import kr.co.webee.domain.post.repository.PostLikeRepository;
import kr.co.webee.domain.post.repository.PostRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.post.dto.response.PostLikeToggleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostLikeToggleResponse toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return postLikeRepository.findByPostIdAndUserId(postId, userId)
                .map(postLike -> {
                    postLikeRepository.delete(postLike);

                    return PostLikeToggleResponse.of(
                            false,
                            postLikeRepository.countByPostId(postId)
                    );

                })
                .orElseGet(() -> {
                    postLikeRepository.save(
                            PostLike.builder()
                                    .post(post)
                                    .user(user)
                                    .build()
                    );

                    return PostLikeToggleResponse.of(
                            true,
                            postLikeRepository.countByPostId(postId)
                    );
                });
    }
}
