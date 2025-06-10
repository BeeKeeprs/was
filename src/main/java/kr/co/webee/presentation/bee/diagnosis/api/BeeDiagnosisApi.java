package kr.co.webee.presentation.bee.diagnosis.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.application.bee.diagnosis.BeeDiagnosisSaveResponse;
import kr.co.webee.presentation.bee.diagnosis.dto.*;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "꿀벌 질병 진단", description = "꿀벌 질병 진단 관련 API")
public interface BeeDiagnosisApi {
    @Operation(
            summary = "꿀벌 질병 진단",
            description = "꿀벌 이미지를 multipart/form-data로 전송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "꿀벌 질병 진단 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @PostMapping()
    BeeDiagnosisResponse diagnoseBeeDisease(
            @Parameter(
                    description = "꿀벌 이미지",
                    array = @ArraySchema(
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam(value = "beeImage", required = true) MultipartFile image);


    @Operation(
            summary = "꿀벌 이미지 질병 판단 결과 기반 맞춤형 AI 대처 방안 요청 API",
            description = "꿀벌 이미지 질병 판단 결과와 사용자 농지 정보를 바탕으로 AI가 맞춤형 대처 방안을 응답합니다."
    )
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/ai")
    BeeDiseaseAiSolutionResponse getBeeDiseaseAiCustomSolution(@RequestBody BeeDiseaseAndUserCropInfoRequest request);


    @Operation(
            summary = "꿀벌 이미지 질병 진단 결과 저장 API",
            description = "multipart/form-data로 꿀벌 이미지와 질병 진단 결과를 함께 전송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "꿀벌 이미지 질병 진단 결과 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BeeDiagnosisSaveResponse saveBeeDiagnosis(
            @Parameter(
                    description = "꿀벌 질병 진단 결과 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BeeDiagnosisRequest.class)
                    )
            )
            @RequestPart("request") BeeDiagnosisRequest request,

            @Parameter(
                    description = "꿀벌 이미지",
                    required = true,
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestPart(value = "beeImage", required = true) MultipartFile image,

            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "꿀벌 질병 진단 결과 목록 조회",
            description = "꿀벌 질병 진단 결과 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = BeeDiagnosisListResponse.class))
                    )
            )
    })
    List<BeeDiagnosisListResponse> getBeeDiagnosisList(
            @Parameter(hidden = true)
            @UserId Long userId);
}
