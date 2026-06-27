package kr.co.webee.application.community.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.community.dto.response.ActiveUserResponse;
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

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("활동 중인 유저 목록 조회")
    class GetActiveUsers {

        @Test
        @DisplayName("최근 1시간 내 활동한 유저 목록을 반환한다.")
        void getActiveUsers() {
            //given
            User activeUser = TestFixture.createUser("active-user");
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
            User oldUser = TestFixture.createUser("old-user");
            oldUser.updateLastActivityAt(LocalDateTime.now().minusHours(2));
            userRepository.save(oldUser);

            userRepository.save(TestFixture.createUser("no-activity-user"));

            //when
            List<ActiveUserResponse> result = communityService.getActiveUsers();

            //then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("활동 중인 유저의 userId, name, profileImageUrl을 반환한다.")
        void getActiveUsersResponseFields() {
            //given
            User activeUser = TestFixture.createUser("active-user");
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
}
