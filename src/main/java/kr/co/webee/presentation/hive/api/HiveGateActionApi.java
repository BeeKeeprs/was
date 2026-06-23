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
import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
import kr.co.webee.application.hive.dto.request.HiveGateActionUpdateRequest;
import kr.co.webee.application.hive.dto.response.HiveGateActionDetailResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionListResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionRegisterResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    @Operation(summary = "개폐기 동작 목록 조회", description = "벌통에 등록된 개폐기 동작 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = HiveGateActionListResponse.class))
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    List<HiveGateActionListResponse> getAllHiveGateActionList(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "개폐기 동작 단건 조회", description = "벌통에 등록된 특정 개폐기 동작을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveGateActionDetailResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType({ErrorType.HIVE_NOT_FOUND, ErrorType.HIVE_GATE_ACTION_NOT_FOUND})
    HiveGateActionDetailResponse getHiveGateAction(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(description = "개폐기 동작 ID", example = "1", required = true)
            @PathVariable Long actionId,

            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "개폐기 동작 수정", description = "벌통에 등록된 개폐기 동작을 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveGateActionDetailResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType({ErrorType.HIVE_NOT_FOUND, ErrorType.HIVE_GATE_ACTION_NOT_FOUND})
    HiveGateActionDetailResponse updateHiveGateAction(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,

            @Parameter(description = "개폐기 동작 ID", example = "1", required = true)
            @PathVariable Long actionId,

            @Parameter(hidden = true) @UserId Long userId,

            @Parameter(
                    description = "개폐기 동작 수정 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveGateActionUpdateRequest.class)
                    )
            )
            @RequestBody @Valid HiveGateActionUpdateRequest request
    );
}
