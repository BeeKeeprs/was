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

import java.time.Duration;

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

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInDto signInDto) {
        JwtTokenDto jwtTokenDto = authService.signIn(signInDto);
        return createTokenResponse(jwtTokenDto);
    }

    private ResponseEntity<?> createTokenResponse(JwtTokenDto jwtTokenDto) {
        ResponseCookie cookie = CookieUtil.createCookie(JwtConstants.REFRESH_TOKEN_COOKIE_KEY, jwtTokenDto.refreshToken(), Duration.ofDays(7).toSeconds());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, JwtConstants.ACCESS_TOKEN_HEADER_PREFIX + jwtTokenDto.accessToken())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("성공 응답");
    }
}