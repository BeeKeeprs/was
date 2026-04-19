package kr.co.webee.application.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.post.entity.Comment;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.repository.CommentRepository;
import kr.co.webee.domain.post.repository.PostRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.post.dto.request.CommentCreateRequest;
import kr.co.webee.presentation.post.dto.response.CommentCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse createComment(Long postId, CommentCreateRequest request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = commentRepository.save(request.toEntity(post, user));

        return CommentCreateResponse.of(comment.getId());
    }
}
