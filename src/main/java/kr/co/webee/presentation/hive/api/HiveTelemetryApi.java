package kr.co.webee.presentation.hive.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.domain.hive.type.SensorType;
import kr.co.webee.application.hive.dto.response.HiveTelemetryResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "벌통 센서 API", description = "벌통 센서 데이터 관련 API")
public interface HiveTelemetryApi {

    @Operation(summary = "센서 데이터 조회", description = "벌통의 센서 데이터를 기간별로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveTelemetryResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    HiveTelemetryResponse getTelemetry(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(hidden = true) @UserId
            Long userId,

            @Parameter(description = "조회 기간", example = "DAY", required = true)
            @RequestParam Period period,

            @Parameter(description = "센서 타입", example = "INTERNAL_TEMPERATURE", required = true)
            @RequestParam SensorType sensorType
    );
}
