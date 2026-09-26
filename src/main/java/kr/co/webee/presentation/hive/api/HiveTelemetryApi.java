package kr.co.webee.presentation.hive.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.domain.hive.type.Interval;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.application.hive.dto.response.HiveTelemetryResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Tag(name = "벌통 센서 API", description = "벌통 센서 데이터 관련 API")
public interface HiveTelemetryApi {

    @Operation(summary = "센서 데이터 조회", description = "벌통의 전체 센서 데이터를 기간별로 조회합니다. period=HOUR 시 from, interval 필수.")
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

            @Parameter(description = "조회 기간 (HOUR/DAY/WEEK/MONTH)", example = "DAY", required = true)
            @RequestParam Period period,

            @Parameter(description = "조회 시작 시각 (period=HOUR 시 필수, ISO-8601)", example = "2026-09-08T10:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,

            @Parameter(description = "집계 간격 (period=HOUR 시 필수, TEN_SEC/ONE_MIN/FIVE_MIN/TEN_MIN)", example = "FIVE_MIN")
            @RequestParam(required = false) Interval interval
    );
}
