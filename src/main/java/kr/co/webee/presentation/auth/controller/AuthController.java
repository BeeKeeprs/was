package kr.co.webee.presentation.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.dto.SignInResponse;
import kr.co.webee.application.auth.service.AuthService;
import kr.co.webee.common.auth.security.CustomUserDetails;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.common.util.cookie.CookieUtil;
import kr.co.webee.common.util.jwt.JwtConstants;
import kr.co.webee.presentation.auth.api.AuthApi;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.presentation.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

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

    @Override
    @PostMapping("/sign-in")
    public Map<String,String> signIn(@RequestBody @Valid SignInRequest request, HttpServletResponse response) {
        SignInResponse signInResponse = authService.signIn(request);
        createTokenResponse(signInResponse.jwtTokenDto(), response);
        return Map.of("name", signInResponse.name());
    }

    @Override
    @PostMapping("/reissue")
    public String reissueToken(@CookieValue(name = JwtConstants.REFRESH_TOKEN_COOKIE_KEY, required = false) String refreshToken,
                             HttpServletResponse response) {
        if (refreshToken == null) {
            throw new BusinessException(ErrorType.COOKIE_NOT_FOND);
        }
        JwtTokenDto jwtTokenDto = authService.reissueToken(refreshToken);
        createTokenResponse(jwtTokenDto, response);
        return "OK";
    }

    @Override
    @PostMapping("/sign-out")
    public String signOut(@CookieValue(name = JwtConstants.REFRESH_TOKEN_COOKIE_KEY, required = false) String refreshToken,
                                     HttpServletResponse response) {
        authService.signOut(refreshToken, response);
        return "OK";
    }

    private void createTokenResponse(JwtTokenDto jwtTokenDto, HttpServletResponse response) {
        ResponseCookie cookie = CookieUtil.createCookie(JwtConstants.REFRESH_TOKEN_COOKIE_KEY, jwtTokenDto.refreshToken(), Duration.ofDays(7).toSeconds());

        response.addHeader(HttpHeaders.AUTHORIZATION, JwtConstants.ACCESS_TOKEN_HEADER_PREFIX + jwtTokenDto.accessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}