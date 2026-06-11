package kr.co.webee.presentation.fcmtoken.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.fcmtoken.dto.request.FcmTokenRegisterRequest;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "FCM 토큰 API", description = "FCM 토큰 관련 API")
public interface FcmTokenApi {

    @Operation(summary = "FCM 토큰 등록", description = "사용자의 FCM 토큰을 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "string", example = "OK")
                    )
            ),
    })
    String registerToken(
            @Parameter(hidden = true) @UserId Long userId,

            @Parameter(
                    description = "FCM 토큰 등록 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FcmTokenRegisterRequest.class)
                    )
            )
            @RequestBody @Valid FcmTokenRegisterRequest request
    );

    @Operation(summary = "FCM 토큰 삭제", description = "로그아웃 시 해당 기기의 FCM 토큰을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "string", example = "OK")
                    )
            ),
    })
    String deleteToken(
            @Parameter(hidden = true) @UserId Long userId,

            @Parameter(description = "기기 정보", required = true)
            @RequestParam String deviceInfo
    );
}
