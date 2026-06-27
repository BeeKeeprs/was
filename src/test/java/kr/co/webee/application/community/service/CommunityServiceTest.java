package kr.co.webee.application.community.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.repository.PostRepository;
import kr.co.webee.domain.post.type.PostCategory;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.community.dto.response.ActiveUserResponse;
import kr.co.webee.presentation.community.dto.response.TrendingCategoryResponse;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class CommunityServiceTest {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;

    @BeforeEach
    void setUp() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        user = userRepository.save(TestFixture.createUser(null));
    }

    @Nested
    @DisplayName("활동 중인 유저 목록 조회")
    class GetActiveUsers {

        @Test
        @DisplayName("최근 1시간 내 활동한 유저 목록을 반환한다.")
        void getActiveUsers() {
            //given
            User activeUser = userRepository.save(TestFixture.createUser("active-user"));
            activeUser.updateLastActivityAt(LocalDateTime.now().minusMinutes(30));
            userRepository.save(activeUser);

            //when
            List<ActiveUserResponse> result = communityService.getActiveUsers();

            //then
            assertThat(result).hasSize(1)
                    .extracting("name")
                    .containsExactly("테스트유저");
        }

        @Test
        @DisplayName("활동 중인 유저가 없으면 빈 목록을 반환한다.")
        void getActiveUsersEmpty() {
            //given
            User oldUser = userRepository.save(TestFixture.createUser("old-user"));
            oldUser.updateLastActivityAt(LocalDateTime.now().minusHours(2));
            userRepository.save(oldUser);

            //when
            List<ActiveUserResponse> result = communityService.getActiveUsers();

            //then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("활동 중인 유저의 userId, name, profileImageUrl을 반환한다.")
        void getActiveUsersResponseFields() {
            //given
            User activeUser = userRepository.save(TestFixture.createUser("active-user"));
            activeUser.updateLastActivityAt(LocalDateTime.now().minusMinutes(10));
            activeUser.updateProfileImageUrl("https://example.com/profile.jpg");
            User saved = userRepository.save(activeUser);

            //when
            List<ActiveUserResponse> result = communityService.getActiveUsers();

            //then
            assertThat(result).hasSize(1);
            ActiveUserResponse response = result.get(0);
            assertThat(response.userId()).isEqualTo(saved.getId());
            assertThat(response.name()).isEqualTo("테스트유저");
            assertThat(response.profileImageUrl()).isEqualTo("https://example.com/profile.jpg");
        }
    }

    @Nested
    @DisplayName("지금 뜨는 주제 조회")
    class GetTrendingCategories {

        @Test
        @DisplayName("최근 24시간 내 게시글이 많은 카테고리 순으로 반환한다.")
        void getTrendingCategories() {
            //given
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.QUESTION, user));
            postRepository.save(TestFixture.createPost(PostCategory.QUESTION, user));
            postRepository.save(TestFixture.createPost(PostCategory.NEWS, user));

            //when
            List<TrendingCategoryResponse> result = communityService.getTrendingCategories();

            //then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).category()).isEqualTo(PostCategory.KNOWHOW);
            assertThat(result.get(0).postCount()).isEqualTo(3);
            assertThat(result.get(1).category()).isEqualTo(PostCategory.QUESTION);
            assertThat(result.get(1).postCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("최대 3개 카테고리만 반환한다.")
        void getTrendingCategoriesTopN() {
            //given
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.QUESTION, user));
            postRepository.save(TestFixture.createPost(PostCategory.NEWS, user));
            postRepository.save(TestFixture.createPost(PostCategory.MARKET, user));

            //when
            List<TrendingCategoryResponse> result = communityService.getTrendingCategories();

            //then
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("최근 24시간 내 게시글이 없으면 빈 목록을 반환한다.")
        void getTrendingCategoriesEmpty() {
            //when
            List<TrendingCategoryResponse> result = communityService.getTrendingCategories();

            //then
            assertThat(result).isEmpty();
        }
    }
}
