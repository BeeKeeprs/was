package kr.co.webee.application.auth.service;

import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.helper.JwtHelper;
import kr.co.webee.common.auth.JwtProvider;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.application.user.service.UserService;
import kr.co.webee.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

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

            when(userService.existsByUsername(request.username())).thenReturn(false);
            when(passwordEncoder.encode("password")).thenReturn(encodedPassword);

            //when
            authService.signup(request);

            //then
            verify(userService).save(userCaptor.capture());

            assertEquals(request.username(), userCaptor.getValue().getUsername());
            assertEquals(userCaptor.getValue().getName(), request.name());
        }

        @Test
        @DisplayName("실패 - 동일한 아이디를 가진 사용자가 존재하는 경우")
        void signUpFailSameUsername() {
            //given
            String encodedPassword = "encodedPassword";

            when(userService.existsByUsername(request.username())).thenReturn(true);

            //when
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.signup(request));

            //then
            assertEquals(ErrorType.ALREADY_EXIST_USERNAME, exception.getType());
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
            when(userService.readByUsername(request.username())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
            when(jwtHelper.createToken(any(), any())).thenReturn(jwtTokenDto);

            //when
            JwtTokenDto result = authService.signIn(request);

            //then
            assertNotNull(result);
            assertEquals("accessToken", result.accessToken());
            assertEquals("refreshToken", result.refreshToken());
        }

        @Test
        @DisplayName("실패 - 아이디가 존재하지 않는 경우")
        void signInFailNotFoundUsername() {
            //given
            when(userService.readByUsername(request.username())).thenReturn(Optional.empty());

            //when
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.signIn(request));

            //then
            assertEquals(exception.getType(), ErrorType.INVALID_CREDENTIALS);
        }

        @Test
        @DisplayName("실패 - 비밀번호가 일치하지 않는 경우")
        void signInFailPasswordMismatch() {
            //given
            when(userService.readByUsername(request.username())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

            //when
            BusinessException exception = assertThrows(BusinessException.class, () -> authService.signIn(request));

            // then
            assertEquals(exception.getType(), ErrorType.FAILED_AUTHENTICATION);
        }
    }
}