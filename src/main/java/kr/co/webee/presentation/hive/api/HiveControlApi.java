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
import kr.co.webee.presentation.hive.dto.request.HiveAutoControlRequest;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "벌통 센서 제어 API", description = "벌통 센서 제어 관련 API")
public interface HiveControlApi {

    @Operation(summary = "센서 자동제어 설정", description = "벌통 센서의 자동제어 활성화 여부를 설정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "설정 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "string", example = "OK")
                    )
            ),
    })
    @ApiDocsErrorType({ErrorType.HIVE_NOT_FOUND, ErrorType.HIVE_AUTO_CONTROL_BLOCKED_BY_MANUAL, ErrorType.HIVE_AUTO_CONTROL_BLOCKED_BY_SCHEDULE})
    String setAutoControl(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(hidden = true) @UserId Long userId,

            @Parameter(
                    description = "자동제어 설정 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveAutoControlRequest.class)
                    )
            )
            @RequestBody @Valid HiveAutoControlRequest request
    );
}
