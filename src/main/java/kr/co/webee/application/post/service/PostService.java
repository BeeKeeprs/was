package kr.co.webee.application.post.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.repository.PostRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.post.dto.request.PostCreateRequest;
import kr.co.webee.presentation.post.dto.response.PostCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.save(request.toEntity(user));

        return PostCreateResponse.of(post.getId());
    }
}
