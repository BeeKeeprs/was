package kr.co.webee.domain.post.repository;

import kr.co.webee.domain.annotation.RepositoryTest;
import kr.co.webee.domain.post.entity.Post;
import kr.co.webee.domain.post.type.PostCategory;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        user = userRepository.save(TestFixture.createUser(null));
    }

    @Nested
    @DisplayName("카테고리별 게시글 수 집계")
    class FindTopCategoriesSince {

        @Test
        @DisplayName("기준 시각 이후 게시글을 카테고리별로 집계하여 내림차순으로 반환한다.")
        void findTopCategoriesSince() {
            //given
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.QUESTION, user));
            postRepository.save(TestFixture.createPost(PostCategory.QUESTION, user));
            postRepository.save(TestFixture.createPost(PostCategory.NEWS, user));

            //when
            List<CategoryCount> result = postRepository.findTopCategoriesSince(
                    java.time.LocalDateTime.now().minusHours(24),
                    PageRequest.of(0, 10)
            );

            //then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).category()).isEqualTo(PostCategory.KNOWHOW);
            assertThat(result.get(0).count()).isEqualTo(3);
            assertThat(result.get(1).category()).isEqualTo(PostCategory.QUESTION);
            assertThat(result.get(1).count()).isEqualTo(2);
            assertThat(result.get(2).category()).isEqualTo(PostCategory.NEWS);
            assertThat(result.get(2).count()).isEqualTo(1);
        }

        @Test
        @DisplayName("기준 시각 이전 게시글은 집계에 포함되지 않는다.")
        void findTopCategoriesSinceExcludesOld() {
            //given
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));

            //when
            List<CategoryCount> result = postRepository.findTopCategoriesSince(
                    java.time.LocalDateTime.now().plusHours(1),
                    PageRequest.of(0, 10)
            );

            //then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("게시글이 없으면 빈 목록을 반환한다.")
        void findTopCategoriesSinceEmpty() {
            //when
            List<CategoryCount> result = postRepository.findTopCategoriesSince(
                    java.time.LocalDateTime.now().minusHours(24),
                    PageRequest.of(0, 10)
            );

            //then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Pageable limit으로 상위 N개 카테고리만 반환한다.")
        void findTopCategoriesSinceWithLimit() {
            //given
            postRepository.save(TestFixture.createPost(PostCategory.KNOWHOW, user));
            postRepository.save(TestFixture.createPost(PostCategory.QUESTION, user));
            postRepository.save(TestFixture.createPost(PostCategory.NEWS, user));
            postRepository.save(TestFixture.createPost(PostCategory.MARKET, user));

            //when
            List<CategoryCount> result = postRepository.findTopCategoriesSince(
                    java.time.LocalDateTime.now().minusHours(24),
                    PageRequest.of(0, 2)
            );

            //then
            assertThat(result).hasSize(2);
        }
    }
}
