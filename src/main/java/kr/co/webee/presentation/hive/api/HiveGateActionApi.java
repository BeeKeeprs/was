package kr.co.webee.presentation.hive.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
import kr.co.webee.application.hive.dto.response.HiveGateActionRegisterResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "개폐기 동작 API", description = "벌통 개폐기 동작 관련 API")
public interface HiveGateActionApi {

    @Operation(summary = "개폐기 동작 등록", description = "벌통 개폐기 동작을 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveGateActionRegisterResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    HiveGateActionRegisterResponse registerHiveGateAction(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(hidden = true) @UserId Long userId,

            @Parameter(
                    description = "개폐기 동작 등록 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveGateActionRegisterRequest.class)
                    )
            )
            @RequestBody @Valid HiveGateActionRegisterRequest request
    );
}
