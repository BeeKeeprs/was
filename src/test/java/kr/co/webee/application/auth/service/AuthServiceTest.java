package kr.co.webee.application.auth.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.dto.SignInDto;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("회원가입")
    class SignUp {
        private final SignUpRequest request = SignUpRequest.builder()
                .username("username")
                .password("password")
                .name("name")
                .build();

        @Test
        @DisplayName("성공")
        void signUpSuccess() {
            //when
            authService.signup(request);

            //then
            User savedUser = userRepository.findByUsername(request.username()).orElseThrow();
            assertThat(savedUser.getUsername()).isEqualTo(request.username());
            assertThat(savedUser.getName()).isEqualTo(request.name());
        }

        @Test
        @DisplayName("실패 - 동일한 아이디를 가진 사용자가 존재하는 경우")
        void signUpFailSameUsername() {
            //given
            authService.signup(request);

            //when - then
            assertThatThrownBy(() -> authService.signup(request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.ALREADY_EXIST_USERNAME);
        }
    }

    @Nested
    @DisplayName("로그인")
    class SignIn {
        private final String rawPassword = "password";

        private final SignInRequest request = SignInRequest.builder()
                .username("username")
                .password(rawPassword)
                .build();

        @Test
        @DisplayName("성공")
        void signInSuccess() {
            //given
            userRepository.save(User.builder()
                    .username("username")
                    .password(passwordEncoder.encode(rawPassword))
                    .name("name")
                    .build());

            //when
            SignInDto response = authService.signIn(request);

            //then
            assertThat(response).isNotNull();
            assertThat(response.name()).isEqualTo("name");
            JwtTokenDto token = response.jwtTokenDto();
            assertThat(token.accessToken()).isNotNull();
            assertThat(token.refreshToken()).isNotNull();
        }

        @Test
        @DisplayName("실패 - 아이디가 존재하지 않는 경우")
        void signInFailNotFoundUsername() {
            //when - then
            assertThatThrownBy(() -> authService.signIn(request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.INVALID_CREDENTIALS);
        }

        @Test
        @DisplayName("실패 - 비밀번호가 일치하지 않는 경우")
        void signInFailPasswordMismatch() {
            //given
            userRepository.save(User.builder()
                    .username("username")
                    .password(passwordEncoder.encode("differentPassword"))
                    .name("name")
                    .build());

            //when - then
            assertThatThrownBy(() -> authService.signIn(request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.FAILED_AUTHENTICATION);
        }
    }
}