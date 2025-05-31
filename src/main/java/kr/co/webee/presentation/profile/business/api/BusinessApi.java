package kr.co.webee.presentation.profile.business.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.support.annotation.UserId;
import kr.co.webee.presentation.profile.business.dto.request.BusinessCreateRequest;
import kr.co.webee.presentation.profile.business.dto.response.BusinessCreateResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessDetailResponse;
import kr.co.webee.presentation.profile.business.dto.response.BusinessListResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "업체 API", description = "업체 관련 API")
public interface BusinessApi {
    @Operation(
            summary = "업체 등록",
            description = "업체를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 등록 성공"),
    })
    BusinessCreateResponse createBusiness(
            @Parameter(description = "업체 정보", required = true)
            @RequestBody @Valid BusinessCreateRequest request,

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
            summary = "업체 상세 조회",
            description = "업체의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 업체 없음")
    })
    BusinessDetailResponse getBusinessDetail(@PathVariable Long businessId);
}
