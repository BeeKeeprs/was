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
}
