package kr.co.webee.presentation.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.webee.common.auth.security.CustomUserDetails;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.domain.oauth.enums.OAuthPlatform;
import kr.co.webee.presentation.auth.dto.request.UserInfoRegisterRequest;
import kr.co.webee.presentation.auth.dto.response.OAuthSignInResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "OAuth", description = "소셜 로그인(OAuth) 관련 API")
public interface OAuthApi {

    @Operation(
            summary = "소셜 로그인",
            description = "카카오/네이버 인가 코드로 로그인합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OAuthSignInResponse.class)
            )
    )
    @ApiDocsErrorType(value = {
            ErrorType.UNSUPPORTED_SOCIAL_PLATFORM,
            ErrorType.OAUTH_TOKEN_REQUEST_FAILED,
            ErrorType.OAUTH_USER_INFO_REQUEST_FAILED
    })
    OAuthSignInResponse signIn(
            @Parameter(
                    description = "소셜 플랫폼",
                    required = true,
                    schema = @Schema(implementation = OAuthPlatform.class)
            )
            @PathVariable OAuthPlatform platform,

            @Parameter(
                    description = "소셜 로그인 인가 코드",
                    required = true,
                    schema = @Schema(type = "string"),
                    example = "authorization_code_issued_by_provider"
            )
            @RequestParam String code,

            HttpServletResponse response
    );

    @Operation(
            summary = "소셜 로그인 사용자 정보 등록",
            description = "OAuth로 최초 가입한 사용자의 표시 이름 등 추가 정보를 등록합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "등록 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(description = "성공 응답 본문", type = "string", example = "OK")
            )
    )
    @ApiDocsErrorType(value = {
            ErrorType.FAILED_AUTHENTICATION,
            ErrorType.ENTITY_NOT_FOUND,
            ErrorType.FAILED_VALIDATION
    })
    String registerUserInfo(
            @RequestBody @Valid UserInfoRegisterRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    );
}
