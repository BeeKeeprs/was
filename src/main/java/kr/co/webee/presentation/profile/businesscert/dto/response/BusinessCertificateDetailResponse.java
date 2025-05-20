package kr.co.webee.presentation.profile.businesscert.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.profile.businesscert.entity.BusinessCertificate;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "사업자등록정보 상세 정보 response")
public record BusinessCertificateDetailResponse(
        @Schema(description = "사업자등록정보 ID", example = "1")
        Long businessCertificateId,

        @Schema(description = "상호명", example = "꿀벌 농장")
        String companyName,

        @Schema(description = "사업장 소재지", example = "전라북도 전주시 완산구 홍산로")
        String businessAddress,

        @Schema(description = "사업장 소재지 위도", example = "35.8168945124229")
        Double latitude,

        @Schema(description = "사업장 소재지 경도", example = "127.105944703404")
        Double longitude,

        @Schema(description = "사업자등록번호", example = "1023456798")
        String registrationNumber,

        @Schema(description = "대표자명", example = "홍길동")
        String representativeName,

        @Schema(description = "개업 일자", example = "2020-03-15")
        LocalDate commencementDate,

        @Schema(description = "온라인 스토어 링크", example = "https://smartstore.naver.com/honeybee")
        String onlineStoreUrl

        //사업자등록증 이미지(nullable)
) {
        public static BusinessCertificateDetailResponse from(BusinessCertificate businessCertificate) {
                return BusinessCertificateDetailResponse.builder()
                        .businessCertificateId(businessCertificate.getId())
                        .companyName(businessCertificate.getCompanyName())
                        .businessAddress(businessCertificate.getBusinessAddress())
                        .latitude(businessCertificate.getAddressCoordinates().getLatitude())
                        .longitude(businessCertificate.getAddressCoordinates().getLongitude())
                        .registrationNumber(businessCertificate.getRegistrationNumber())
                        .representativeName(businessCertificate.getRepresentativeName())
                        .commencementDate(businessCertificate.getCommencementDate())
                        .build();
        }
}
