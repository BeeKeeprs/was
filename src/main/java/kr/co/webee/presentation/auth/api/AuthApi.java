package kr.co.webee.presentation.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.auth.dto.request.SignUpRequest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.annotation.ApiDocsErrorType;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth")
public interface AuthApi {
    @Operation(summary = "회원가입", description = "사용자 정보를 등록하는 API")
    @ApiDocsErrorType(ErrorType.ALREADY_EXIST_USERNAME)
    public void signUp(@RequestBody @Valid SignUpRequest signUpRequest);
}
