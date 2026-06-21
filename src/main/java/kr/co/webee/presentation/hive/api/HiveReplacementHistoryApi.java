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
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryCreateRequest;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryUpdateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryCreateResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryDetailResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryListResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "벌통 교체 기록 API", description = "벌통 교체 기록 관련 API")
public interface HiveReplacementHistoryApi {

    @Operation(summary = "벌통 교체 기록 등록", description = "벌통 교체 기록을 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveReplacementHistoryCreateResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    HiveReplacementHistoryCreateResponse createReplacementHistory(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(
                    description = "벌통 교체 기록 등록 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveReplacementHistoryCreateRequest.class)
                    )
            )
            @RequestBody @Valid HiveReplacementHistoryCreateRequest request
    );

    @Operation(summary = "벌통 교체 기록 상세 조회", description = "벌통 교체 기록을 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveReplacementHistoryDetailResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType({ErrorType.HIVE_NOT_FOUND, ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND})
    HiveReplacementHistoryDetailResponse getReplacementHistoryDetail(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(description = "교체 기록 ID", example = "31", required = true)
            @PathVariable Long historyId,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "벌통 교체 기록 목록 조회", description = "벌통 교체 기록 목록을 교체일 기준 최신순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공"
            ),
    })
    @ApiDocsErrorType(ErrorType.HIVE_NOT_FOUND)
    Slice<HiveReplacementHistoryListResponse> getAllReplacementHistories(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(hidden = true) @UserId Long userId,
            Pageable pageable
    );

    @Operation(summary = "벌통 교체 기록 수정", description = "벌통 교체 기록을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
    })
    @ApiDocsErrorType({ErrorType.HIVE_NOT_FOUND, ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND})
    void updateReplacementHistory(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(description = "교체 기록 ID", example = "31", required = true)
            @PathVariable Long historyId,
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(
                    description = "벌통 교체 기록 수정 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HiveReplacementHistoryUpdateRequest.class)
                    )
            )
            @RequestBody @Valid HiveReplacementHistoryUpdateRequest request
    );

    @Operation(summary = "벌통 교체 기록 삭제", description = "벌통 교체 기록을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
    })
    @ApiDocsErrorType({ErrorType.HIVE_NOT_FOUND, ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND})
    void deleteReplacementHistory(
            @Parameter(description = "벌통 ID", example = "1", required = true)
            @PathVariable Long hiveId,
            @Parameter(description = "교체 기록 ID", example = "31", required = true)
            @PathVariable Long historyId,
            @Parameter(hidden = true) @UserId Long userId
    );
}