package kr.co.webee.application.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.dto.SignInResponse;
import kr.co.webee.application.auth.helper.JwtHelper;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.application.user.service.UserService;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignUpRequest request) {
        if (userService.existsByUsername(request.username())) {
            String message = String.format("username: %s", request.username());
            throw new BusinessException(ErrorType.ALREADY_EXIST_USERNAME, message);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = request.toEntity(encodedPassword);
        userService.save(user);
    }

    public SignInResponse signIn(SignInRequest request) {
        User user = userService.readByUsername(request.username())
                .orElseThrow(() -> {
                    String message = String.format("username: %s", request.username());
                    return new BusinessException(ErrorType.INVALID_CREDENTIALS, message);
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorType.FAILED_AUTHENTICATION);
        }

        JwtTokenDto token = jwtHelper.createToken(user.getId(), user.getUsername());
        return SignInResponse.of(user.getName(), token);
    }

    public JwtTokenDto reissueToken(String refreshToken) {
        return jwtHelper.reissueToken(refreshToken);
    }

    public void signOut(String refreshToken, HttpServletResponse response) {
        jwtHelper.deleteRefreshToken(refreshToken, response);
    }
}
