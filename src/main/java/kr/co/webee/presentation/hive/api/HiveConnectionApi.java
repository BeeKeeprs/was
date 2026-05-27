package kr.co.webee.presentation.hive.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.application.hive.dto.response.HiveConnectionResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "벌통 연동 상태 API", description = "벌통 연동 상태 관련 API")
public interface HiveConnectionApi {

    @Operation(summary = "연동 상태 조회", description = "벌통의 연동 상태 및 마지막 연결 시각을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveConnectionResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    HiveConnectionResponse getConnection(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(hidden = true) @UserId Long userId
    );
}
