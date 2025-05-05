package kr.co.webee.domain.user.repository;

import kr.co.webee.domain.annotation.RepositoryTest;
import kr.co.webee.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("username을 가진 User가 있으면 true, 없으면 false를 반환한다.")
    void existsByUsername() {
        //given
        User user = User.builder()
                .username("username")
                .password("password")
                .name("name")
                .build();

        userRepository.save(user);

        //when
        boolean exists = userRepository.existsByUsername("username");
        boolean notExists = userRepository.existsByUsername("notExists");

        //then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}