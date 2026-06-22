package kr.co.webee.application.auth.service;

import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.dto.SignInDto;
import kr.co.webee.application.auth.helper.JwtHelper;
import kr.co.webee.common.auth.jwt.JwtProvider;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private final User user = User.builder()
            .username("username")
            .password("encodedPassword")
            .name("name")
            .build();

    private final JwtTokenDto jwtTokenDto = JwtTokenDto.of("accessToken", "refreshToken");

    @Nested
    @DisplayName("회원가입")
    class SignUp {
        private final SignUpRequest request = SignUpRequest.builder()
                .username("username")
                .password("password")
                .name("name")
                .build();

        @Captor
        private ArgumentCaptor<User> userCaptor;

        @Test
        @DisplayName("성공")
        void signUpSuccess() {
            //given
            String encodedPassword = "encodedPassword";

            when(userRepository.existsByUsername(request.username())).thenReturn(false);
            when(passwordEncoder.encode("password")).thenReturn(encodedPassword);

            //when
            authService.signup(request);

            //then
            verify(userRepository).save(userCaptor.capture());

            assertThat(userCaptor.getValue().getUsername()).isEqualTo(request.username());
            assertThat(userCaptor.getValue().getName()).isEqualTo(request.name());
        }

        @Test
        @DisplayName("실패 - 동일한 아이디를 가진 사용자가 존재하는 경우")
        void signUpFailSameUsername() {
            //given
            String encodedPassword = "encodedPassword";

            when(userRepository.existsByUsername(request.username())).thenReturn(true);

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
        private final SignInRequest request = SignInRequest.builder()
                .username("username")
                .password("password")
                .build();

        @Test
        @DisplayName("성공")
        void signInSuccess() {
            //given
            when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
            when(jwtHelper.createToken(any(), any())).thenReturn(jwtTokenDto);

            //when
            SignInDto response = authService.signIn(request);

            //then
            assertThat(response).isNotNull();
            assertThat(response.name()).isEqualTo("name");
            JwtTokenDto token = response.jwtTokenDto();
            assertThat(token.accessToken()).isEqualTo("accessToken");
            assertThat(token.refreshToken()).isEqualTo("refreshToken");
        }

        @Test
        @DisplayName("실패 - 아이디가 존재하지 않는 경우")
        void signInFailNotFoundUsername() {
            //given
            when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

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
            when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

            //when - then
            assertThatThrownBy(() -> authService.signIn(request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.FAILED_AUTHENTICATION);
        }
    }
}