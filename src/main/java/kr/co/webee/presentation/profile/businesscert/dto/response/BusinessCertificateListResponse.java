package kr.co.webee.presentation.profile.businesscert.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.profile.businesscert.entity.BusinessCertificate;
import lombok.Builder;

@Builder
@Schema(description = "사업자등록정보 목록 response")
public record BusinessCertificateListResponse(
        @Schema(description = "사업자등록정보 ID", example = "1")
        Long businessCertificateId,

        @Schema(description = "상호명", example = "꿀벌 농장")
        String companyName,

        @Schema(description = "사업장 소재지", example = "전라북도 전주시 완산구 홍산로")
        String businessAddress,

        @Schema(description = "사업장 소재지 위도", example = "35.8168945124229")
        Double latitude,

        @Schema(description = "사업장 소재지 경도", example = "127.105944703404")
        Double longitude
) {
    public static BusinessCertificateListResponse from(BusinessCertificate businessCertificate) {
        return BusinessCertificateListResponse.builder()
                .businessCertificateId(businessCertificate.getId())
                .companyName(businessCertificate.getCompanyName())
                .businessAddress(businessCertificate.getBusinessAddress())
                .latitude(businessCertificate.getAddressCoordinates().getLatitude())
                .longitude(businessCertificate.getAddressCoordinates().getLongitude())
                .build();
    }
}
