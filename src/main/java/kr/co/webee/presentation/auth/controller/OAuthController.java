package kr.co.webee.presentation.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.webee.application.auth.dto.JwtTokenDto;
import kr.co.webee.application.auth.dto.OAuthSignInDto;
import kr.co.webee.application.auth.service.OAuthService;
import kr.co.webee.common.auth.security.CustomUserDetails;
import kr.co.webee.common.constant.JwtConstants;
import kr.co.webee.domain.oauth.enums.OAuthPlatform;
import kr.co.webee.presentation.auth.api.OAuthApi;
import kr.co.webee.presentation.auth.dto.request.UserInfoRegisterRequest;
import kr.co.webee.presentation.auth.dto.response.OAuthSignInResponse;
import kr.co.webee.presentation.support.util.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController implements OAuthApi {
    private final OAuthService oAuthService;

    @Override
    @GetMapping("/sign-in/{platform}")
    public OAuthSignInResponse signIn(
            @PathVariable OAuthPlatform platform,
            @RequestParam String code,
            HttpServletResponse response
    ) {
        OAuthSignInDto signInDto = oAuthService.signIn(platform, code);
        createTokenResponse(signInDto.jwtTokenDto(), response);
        return OAuthSignInResponse.of(signInDto.isNewUser(), signInDto.name());
    }

    @Override
    @PostMapping("/register")
    public String registerUserInfo(
            @RequestBody @Valid UserInfoRegisterRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        oAuthService.registerUserInfo(request, userDetails.getUserId());
        return "OK";
    }

    private void createTokenResponse(JwtTokenDto jwtTokenDto, HttpServletResponse response) {
        ResponseCookie cookie = CookieUtil.createCookie(JwtConstants.REFRESH_TOKEN_COOKIE_KEY, jwtTokenDto.refreshToken(), Duration.ofDays(7).toSeconds());

        response.addHeader(HttpHeaders.AUTHORIZATION, JwtConstants.ACCESS_TOKEN_HEADER_PREFIX + jwtTokenDto.accessToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
