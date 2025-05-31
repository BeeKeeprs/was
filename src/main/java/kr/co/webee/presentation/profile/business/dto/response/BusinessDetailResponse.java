package kr.co.webee.presentation.profile.business.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.profile.business.entity.Business;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "업체 상세 정보 response")
public record BusinessDetailResponse(
        @Schema(description = "업체 ID", example = "1")
        Long businessId,

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

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "온라인 스토어 링크", example = "https://smartstore.naver.com/honeybee")
        String onlineStoreUrl,

        @Schema(description = "카카오 채팅 url", example = "https://pf.kakao.com/_abcdef")
        String kakaoChatUrl,

        @Schema(description = "사업자등록증 이미지 url", example = "https://adbcds.cloudfront.net/business-certificate/이미지.jpeg")
        String businessCertImageUrl
) {
        public static BusinessDetailResponse from(Business business) {
                return BusinessDetailResponse.builder()
                        .businessId(business.getId())
                        .companyName(business.getCompanyName())
                        .businessAddress(business.getBusinessAddress())
                        .latitude(business.getAddressCoordinates().getLatitude())
                        .longitude(business.getAddressCoordinates().getLongitude())
                        .registrationNumber(business.getRegistrationNumber())
                        .representativeName(business.getRepresentativeName())
                        .commencementDate(business.getCommencementDate())
                        .phoneNumber(business.getPhoneNumber())
                        .onlineStoreUrl(business.getOnlineStoreUrl())
                        .kakaoChatUrl(business.getKakaoChatUrl())
                        .businessCertImageUrl(business.getBusinessCertImageUrl())
                        .build();
        }
}
