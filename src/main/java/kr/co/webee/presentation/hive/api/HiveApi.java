package kr.co.webee.presentation.hive.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.hive.dto.request.HiveRegisterRequest;
import kr.co.webee.presentation.hive.dto.response.HiveRegisterResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Hive", description = "벌통 API")
public interface HiveApi {

    @Operation(summary = "벌통 등록", description = "벌통을 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HiveRegisterResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_SERIAL_NUMBER_ALREADY_EXISTS)
    HiveRegisterResponse registerHive(
            @Parameter(
                    description = "벌통 등록 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveRegisterRequest.class)
                    )
            )
            @RequestBody @Valid HiveRegisterRequest request,
            @Parameter(hidden = true) @UserId Long userId
    );
}
