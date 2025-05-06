package kr.co.webee.presentation.auth.controller;

import jakarta.validation.Valid;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.application.auth.service.AuthService;
import kr.co.webee.presentation.auth.api.AuthApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid SignUpRequest request) {
        authService.signup(request);
    }
}