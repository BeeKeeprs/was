package kr.co.webee.presentation.hive.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.application.hive.dto.request.HiveControlScheduleRegisterRequest;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleListResponse;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleRegisterResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "벌통 센서 자동제어 스케줄 API", description = "벌통 센서 자동제어 스케줄 관련 API")
public interface HiveControlScheduleApi {

    @Operation(summary = "자동제어 스케줄 등록", description = "벌통 센서 자동제어 스케줄을 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveControlScheduleRegisterResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    HiveControlScheduleRegisterResponse registerHiveControlSchedule(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(hidden = true) @UserId Long userId,

            @Parameter(
                    description = "자동제어 스케줄 등록 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveControlScheduleRegisterRequest.class)
                    )
            )
            @RequestBody @Valid HiveControlScheduleRegisterRequest request
    );

    @Operation(summary = "자동제어 스케줄 목록 조회", description = "벌통에 등록된 센서 자동제어 스케줄 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = HiveControlScheduleListResponse.class))
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    List<HiveControlScheduleListResponse> getHiveControlSchedules(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(hidden = true) @UserId Long userId
    );
}
