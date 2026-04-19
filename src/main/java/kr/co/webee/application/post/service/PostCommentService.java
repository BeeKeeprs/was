package kr.co.webee.application.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.entity.PostComment;
import kr.co.webee.domain.post.repository.PostCommentRepository;
import kr.co.webee.domain.post.repository.PostRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.post.dto.request.PostCommentCreateRequest;
import kr.co.webee.presentation.post.dto.request.PostCommentUpdateRequest;
import kr.co.webee.presentation.post.dto.response.PostCommentCreateResponse;
import kr.co.webee.presentation.post.dto.response.PostCommentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PostCommentListResponse> getAllComments(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new BusinessException(ErrorType.POST_NOT_FOUND);
        }

        return postCommentRepository.findByPostId(postId).stream()
                .map(PostCommentListResponse::from)
                .toList();
    }

    @Transactional
    public PostCommentCreateResponse createComment(Long postId, PostCommentCreateRequest request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        PostComment postComment = postCommentRepository.save(request.toEntity(post, user));

        return PostCommentCreateResponse.of(postComment.getId());
    }

    @Transactional
    public void updateComment(Long postId, Long commentId, PostCommentUpdateRequest request, Long userId) {
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_COMMENT_NOT_FOUND));

        if (postComment.isNotWrittenBy(userId)) {
            throw new BusinessException(ErrorType.POST_COMMENT_ACCESS_DENIED);
        }

        postComment.updateContent(request.content());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorType.POST_COMMENT_NOT_FOUND));

        if (postComment.isNotWrittenBy(userId)) {
            throw new BusinessException(ErrorType.POST_COMMENT_ACCESS_DENIED);
        }

        postCommentRepository.delete(postComment);
    }
}
