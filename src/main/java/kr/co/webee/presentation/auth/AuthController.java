package kr.co.webee.presentation.auth;

import jakarta.validation.Valid;
import kr.co.webee.application.auth.dto.SignUpDto;
import kr.co.webee.application.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        authService.signup(signUpDto);
        return ResponseEntity.ok("성공 응답");
    }
}