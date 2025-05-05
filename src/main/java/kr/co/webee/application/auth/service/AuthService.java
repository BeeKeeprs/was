package kr.co.webee.application.auth.service;

import kr.co.webee.application.auth.dto.SignUpDto;
import kr.co.webee.application.user.service.UserService;
import kr.co.webee.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignUpDto signUpDto) {
        if (userService.existsByUsername(signUpDto.username())) {
            throw new RuntimeException("회원 중복 예외");
        }

        String encodedPassword = passwordEncoder.encode(signUpDto.password());
        User user = signUpDto.toEntity(encodedPassword);
        userService.save(user);
    }
}
