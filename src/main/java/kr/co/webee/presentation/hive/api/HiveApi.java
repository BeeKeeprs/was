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
import kr.co.webee.presentation.hive.dto.request.HiveUpdateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveDetailResponse;
import kr.co.webee.presentation.hive.dto.response.HiveListResponse;
import kr.co.webee.presentation.hive.dto.response.HiveRegisterResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "벌통 API", description = "사용자 벌통 관련 API")
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
    @ApiDocsErrorType(ErrorType.HIVE_MAC_ADDRESS_ALREADY_EXISTS)
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

    @Operation(summary = "전체 벌통 목록 조회", description = "전체 벌통 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveListResponse.class)
                    )
            ),
    })
    HiveListResponse getAllHives(@Parameter(hidden = true) @UserId Long userId);

    @Operation(summary = "벌통 상세 조회", description = "벌통 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveDetailResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    HiveDetailResponse getHiveDetail(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "벌통 수정", description = "벌통 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공"
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    void updateHive(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(
                    description = "벌통 수정 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveUpdateRequest.class)
                    )
            )
            @RequestBody @Valid HiveUpdateRequest request
    );

    @Operation(summary = "벌통 삭제", description = "벌통을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공"
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    void deleteHive(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(hidden = true) @UserId Long userId
    );

}
