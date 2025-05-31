package kr.co.webee.presentation.profile.business.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.profile.business.entity.Business;
import lombok.Builder;

@Builder
@Schema(description = "업체 정보 목록 response")
public record BusinessListResponse(
        @Schema(description = "업체 ID", example = "1")
        Long businessId,

        @Schema(description = "상호명", example = "꿀벌 농장")
        String companyName,

        @Schema(description = "사업장 소재지", example = "전라북도 전주시 완산구 홍산로")
        String businessAddress,

        @Schema(description = "사업장 소재지 위도", example = "35.8168945124229")
        Double latitude,

        @Schema(description = "사업장 소재지 경도", example = "127.105944703404")
        Double longitude
) {
    public static BusinessListResponse from(Business business) {
        return BusinessListResponse.builder()
                .businessId(business.getId())
                .companyName(business.getCompanyName())
                .businessAddress(business.getBusinessAddress())
                .latitude(business.getAddressCoordinates().getLatitude())
                .longitude(business.getAddressCoordinates().getLongitude())
                .build();
    }
}
