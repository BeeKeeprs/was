package kr.co.webee.presentation.profile.businesscert.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.annotation.UserId;
import kr.co.webee.presentation.profile.businesscert.dto.request.BusinessCertificateCreateRequest;
import kr.co.webee.presentation.profile.businesscert.dto.response.BusinessCertificateCreateResponse;
import org.springframework.web.bind.annotation.RequestBody;

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
}
