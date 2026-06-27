package kr.co.webee.application.community.service;

import kr.co.webee.domain.post.repository.PostRepository;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.community.dto.response.ActiveUserResponse;
import kr.co.webee.presentation.community.dto.response.TrendingCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private static final int TRENDING_TOP_N = 3;

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<ActiveUserResponse> getActiveUsers() {
        LocalDateTime since = LocalDateTime.now().minusHours(1);

        return userRepository.findByLastActivityAtAfter(since).stream()
                .map(ActiveUserResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TrendingCategoryResponse> getTrendingCategories() {
        LocalDateTime since = LocalDateTime.now().minusHours(24);

        return postRepository.findTopCategoriesSince(since, PageRequest.of(0, TRENDING_TOP_N))
                .stream()
                .map(row -> TrendingCategoryResponse.of(row.category(), row.count()))
                .toList();
    }
}
