package kr.co.webee.application.auth.service;

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
            String message=String.format("username: %s", request.username());
            throw new BusinessException(ErrorType.ALREADY_EXIST_USERNAME, message);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = request.toEntity(encodedPassword);
        userService.save(user);
    }

    public JwtTokenDto signIn(SignInDto signInDto) {
        User user = userService.readByUsername(signInDto.username())
                .orElseThrow(() -> new RuntimeException("사용자 존재하지 않음 예외"));

        if (!passwordEncoder.matches(signInDto.password(), user.getPassword())) {
            throw new RuntimeException("비밀번호 일치하지 않음 예외");
        }

        return jwtHelper.createToken(user.getId(), user.getUsername());
    }

    public JwtTokenDto reissueToken(String refreshToken) {
        return jwtHelper.reissueToken(refreshToken);
    }
}
