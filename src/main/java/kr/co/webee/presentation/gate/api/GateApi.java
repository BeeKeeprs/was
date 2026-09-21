package kr.co.webee.presentation.gate.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.gate.dto.request.GateRegisterRequest;
import kr.co.webee.presentation.gate.dto.request.GateUpdateRequest;
import kr.co.webee.presentation.gate.dto.response.GateDetailResponse;
import kr.co.webee.presentation.gate.dto.response.GateListResponse;
import kr.co.webee.presentation.gate.dto.response.GateRegisterResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "개폐기 API", description = "개폐기 관련 API")
public interface GateApi {

    @Operation(summary = "개폐기 등록", description = "개폐기를 등록합니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "등록 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GateRegisterResponse.class))))
    @ApiDocsErrorType(ErrorType.GATE_MAC_ADDRESS_ALREADY_EXISTS)
    GateRegisterResponse registerGate(
            @Parameter(description = "개폐기 등록 요청 JSON", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GateRegisterRequest.class)))
            @RequestBody @Valid GateRegisterRequest request,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "개폐기 목록 조회", description = "내 개폐기 전체 목록을 조회합니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GateListResponse.class))))
    GateListResponse getAllGates(@Parameter(hidden = true) @UserId Long userId);

    @Operation(summary = "개폐기 상세 조회", description = "개폐기 상세 정보를 조회합니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GateDetailResponse.class))))
    @ApiDocsErrorType(ErrorType.GATE_NOT_FOUND)
    GateDetailResponse getGateDetail(
            @Parameter(description = "개폐기 ID") @PathVariable Long gateId,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "개폐기 수정", description = "개폐기 정보를 수정합니다.")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "수정 성공"))
    @ApiDocsErrorType(ErrorType.GATE_NOT_FOUND)
    ResponseEntity<Void> updateGate(
            @Parameter(description = "개폐기 ID") @PathVariable Long gateId,
            @RequestBody @Valid GateUpdateRequest request,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "개폐기 삭제", description = "개폐기를 삭제합니다.")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "삭제 성공"))
    @ApiDocsErrorType(ErrorType.GATE_NOT_FOUND)
    ResponseEntity<Void> deleteGate(
            @Parameter(description = "개폐기 ID") @PathVariable Long gateId,
            @Parameter(hidden = true) @UserId Long userId
    );
}
