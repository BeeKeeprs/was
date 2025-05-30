package kr.co.webee.presentation.profile.businesscert.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.profile.businesscert.dto.request.BusinessCertificateCreateRequest;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateCreateResponse;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateDetailResponse;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateListResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "사업자등록정보 API", description = "사업자등록정보 관련 API")
public interface BusinessCertificateApi {
    @Operation(
            summary = "사업자등록정보 등록",
            description = "사업자등록정보를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사업자등록정보 등록 성공"),
    })
    BusinessCertificateCreateResponse createBusinessCertificate(
            @Parameter(description = "사업자등록정보", required = true)
            @RequestBody @Valid BusinessCertificateCreateRequest request,

            @Parameter(hidden = true)
            @UserId Long userId
    );

    @Operation(
            summary = "사업자등록정보 목록 조회",
            description = "사업자등록정보 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사업자등록정보 목록 조회 성공"),
    })
    List<BusinessCertificateListResponse> getBusinessCertificateList();

    @Operation(
            summary = "사업자등록정보 상세 조회",
            description = "사업자등록정보 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사업자등록정보 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사업자등록정보 없음")
    })
    BusinessCertificateDetailResponse getBusinessCertificateDetail(@PathVariable Long businessCertificateId);
}
