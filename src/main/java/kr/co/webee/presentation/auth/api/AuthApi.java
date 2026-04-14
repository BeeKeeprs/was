package kr.co.webee.presentation.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.webee.common.constant.JwtConstants;
import kr.co.webee.presentation.auth.dto.request.PreOrderPhoneRequest;
import kr.co.webee.presentation.auth.dto.request.SignInRequest;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.presentation.auth.dto.request.SmsVerificationSendRequest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.auth.dto.response.PreOrderCheckResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.auth.dto.response.SignInResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "사용자 인증 관련 API")
public interface AuthApi {
    @Operation(summary = "회원가입", description = "사용자 정보를 등록하는 API")
    @ApiDocsErrorType(ErrorType.ALREADY_EXIST_USERNAME)
    void signUp(@RequestBody @Valid SignUpRequest signUpRequest);

    @Operation(summary = "로그인", description = "사용자 인증을 하는 API")
    @ApiDocsErrorType(value = {ErrorType.INVALID_CREDENTIALS, ErrorType.FAILED_AUTHENTICATION})
    SignInResponse signIn(@RequestBody @Valid SignInRequest request, HttpServletResponse response);

    @Operation(summary = "access token 재발급", description = "refresh token을 통해 access token을 재발급 받는 API")
    @ApiDocsErrorType(value = {ErrorType.COOKIE_NOT_FOND, ErrorType.INVALID_ACCESS_TOKEN})
    String reissueToken(@CookieValue(name = JwtConstants.REFRESH_TOKEN_COOKIE_KEY, required = false) String refreshToken,
                                   HttpServletResponse response);

    @Operation(summary = "로그아웃", description = "사용자의 인증 정보를 삭제하는 API")
    String signOut(@CookieValue(name = JwtConstants.REFRESH_TOKEN_COOKIE_KEY, required = false) String refreshToken,
                 HttpServletResponse response);

    @Operation(summary = "SMS 인증번호 발송", description = "휴대전화 인증번호를 발송하는 API")
    @ApiDocsErrorType(value = {ErrorType.SMS_SEND_FAILED})
    String sendSmsVerificationCode(@RequestBody @Valid SmsVerificationSendRequest request);

    @Operation(summary = "사전예약용 전화번호 등록", description = "사전예약용 전화번호를 등록하는 API")
    void savePreOrderPhoneNumber(@RequestBody @Valid PreOrderPhoneRequest request);

    @Operation(summary = "사전예약 여부 확인", description = "사전예약 여부를 확인하는 API")
    PreOrderCheckResponse checkPreOrder(@UserId Long userId);
}

