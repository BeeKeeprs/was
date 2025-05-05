package kr.co.webee.application.auth.service;

import kr.co.webee.application.auth.dto.SignUpDto;
import kr.co.webee.application.user.service.UserService;
import kr.co.webee.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUp{
        @Captor
        private ArgumentCaptor<User> userCaptor;

        @Test
        @DisplayName("성공")
        void signUp_success() {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .username("username")
                    .password("password")
                    .name("name")
                    .build();

            String encodedPassword="encodedPassword";

            when(userService.existsByUsername(signUpDto.username())).thenReturn(false);
            when(passwordEncoder.encode("password")).thenReturn(encodedPassword);

            //when
            authService.signup(signUpDto);

            //then
            verify(userService).save(userCaptor.capture());

            assertEquals(signUpDto.username(), userCaptor.getValue().getUsername());
            assertEquals(userCaptor.getValue().getName(), signUpDto.name());
        }

        @Test
        @DisplayName("실패 - 동일한 username을 가진 user가 존재하는 경우")
        void signUp_fail_same_username() {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .username("username")
                    .password("password")
                    .name("name")
                    .build();

            String encodedPassword="encodedPassword";

            when(userService.existsByUsername(signUpDto.username())).thenReturn(true);

            //when
            RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signup(signUpDto));

            //then
            assertEquals("회원 중복 예외", exception.getMessage());
        }
    }

}