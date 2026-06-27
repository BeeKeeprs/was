package kr.co.webee.domain.user.repository;

import kr.co.webee.domain.annotation.RepositoryTest;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("최근 활동 시각 기준 유저 조회")
    class FindByLastActivityAtAfter {

        @Test
        @DisplayName("기준 시각 이후 활동한 유저를 반환한다.")
        void findByLastActivityAtAfter() {
            //given
            LocalDateTime now = LocalDateTime.now();
            User activeUser = TestFixture.createUser("active-user");
            activeUser.updateLastActivityAt(now.minusMinutes(30));
            userRepository.save(activeUser);

            //when
            List<User> result = userRepository.findByLastActivityAtAfter(now.minusHours(1));

            //then
            assertThat(result).hasSize(1)
                    .extracting("username")
                    .containsExactly("active-user");
        }

        @Test
        @DisplayName("기준 시각 이전에 활동한 유저는 반환하지 않는다.")
        void findByLastActivityAtAfterExcludesOld() {
            //given
            LocalDateTime now = LocalDateTime.now();
            User oldUser = TestFixture.createUser("old-user");
            oldUser.updateLastActivityAt(now.minusHours(2));
            userRepository.save(oldUser);

            //when
            List<User> result = userRepository.findByLastActivityAtAfter(now.minusHours(1));

            //then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("활동 기록이 없는 유저는 반환하지 않는다.")
        void findByLastActivityAtAfterExcludesNoActivity() {
            //given
            LocalDateTime now = LocalDateTime.now();
            userRepository.save(TestFixture.createUser("inactive-user"));

            //when
            List<User> result = userRepository.findByLastActivityAtAfter(now.minusHours(1));

            //then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("활동 중인 유저와 활동 없는 유저가 섞여 있으면 활동 중인 유저만 반환한다.")
        void findByLastActivityAtAfterMixed() {
            //given
            LocalDateTime now = LocalDateTime.now();

            User activeUser1 = TestFixture.createUser("active-user-1");
            activeUser1.updateLastActivityAt(now.minusMinutes(10));
            userRepository.save(activeUser1);

            User activeUser2 = TestFixture.createUser("active-user-2");
            activeUser2.updateLastActivityAt(now.minusMinutes(50));
            userRepository.save(activeUser2);

            User oldUser = TestFixture.createUser("old-user");
            oldUser.updateLastActivityAt(now.minusHours(3));
            userRepository.save(oldUser);

            userRepository.save(TestFixture.createUser("no-activity-user"));

            //when
            List<User> result = userRepository.findByLastActivityAtAfter(now.minusHours(1));

            //then
            assertThat(result).hasSize(2)
                    .extracting("username")
                    .containsExactlyInAnyOrder("active-user-1", "active-user-2");
        }
    }
}
