package kr.co.webee.presentation.profile.business.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.profile.business.dto.request.BusinessCreateRequest;
import kr.co.webee.presentation.profile.business.dto.response.BusinessCreateResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessDetailResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessListResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "업체 API", description = "업체 관련 API")
public interface BusinessApi {
    @Operation(
            summary = "업체 등록",
            description = "업체를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BusinessCreateResponse createBusiness(
            @Parameter(
                    description = "업체 정보",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BusinessCreateRequest.class)
                    )
            )
            @RequestPart("request") BusinessCreateRequest request,

            @Parameter(
                    description = "사업자등록증 이미지(선택)",
                    array = @ArraySchema(
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart(value = "businessCertificateImage", required = false) MultipartFile image,

            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "업체 목록 조회",
            description = "업체 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 목록 조회 성공"),
    })
    List<BusinessListResponse> getBusinessList();

    @Operation(
            summary = "사용자 업체 목록 조회",
            description = "사용자의 업체 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 업체 목록 조회 성공"),
    })
    @GetMapping("/me")
    List<BusinessListResponse> getBusinessListByMe(@UserId Long userId);

    @Operation(
            summary = "업체 상세 조회",
            description = "업체의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 업체 없음")
    })
    BusinessDetailResponse getBusinessDetail(@PathVariable Long businessId);
}
