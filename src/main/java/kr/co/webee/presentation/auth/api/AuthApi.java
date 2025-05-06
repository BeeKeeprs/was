package kr.co.webee.presentation.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.webee.common.util.jwt.JwtConstants;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.response.ApiResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth")
public interface AuthApi {
    @Operation(summary = "회원가입", description = "사용자 정보를 등록하는 API")
    @ApiDocsErrorType(ErrorType.ALREADY_EXIST_USERNAME)
    void signUp(@RequestBody @Valid SignUpRequest signUpRequest);

    @Operation(summary = "로그인", description = "사용자 인증을 하는 API")
    @ApiDocsErrorType(value = {ErrorType.INVALID_CREDENTIALS, ErrorType.FAILED_AUTHENTICATION})
    ApiResponse<?> signIn(@RequestBody @Valid SignInRequest request, HttpServletResponse response);

    @Operation(summary = "access token 재발급", description = "refresh token을 통해 access token을 재발급 받는 API")
    @ApiDocsErrorType(value = {ErrorType.COOKIE_NOT_FOND, ErrorType.INVALID_ACCESS_TOKEN})
    ApiResponse<?> reissueToken(@CookieValue(name = JwtConstants.REFRESH_TOKEN_COOKIE_KEY, required = false) String refreshToken,
                                   HttpServletResponse response);

    @Operation(summary = "로그아웃", description = "사용자의 인증 정보를 삭제하는 API")
    ApiResponse<?> signOut(@CookieValue(name = JwtConstants.REFRESH_TOKEN_COOKIE_KEY, required = false) String refreshToken,
                 HttpServletResponse response);
}
