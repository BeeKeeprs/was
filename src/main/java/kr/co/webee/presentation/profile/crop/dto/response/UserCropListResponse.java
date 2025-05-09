package kr.co.webee.presentation.profile.crop.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import lombok.Builder;

@Builder
@Schema(description = "사용자 재배 작물 정보")
public record UserCropListResponse(
        @Schema(description = "작물명", example = "딸기")
        String name,

        @Schema(description = "품종", example = "설향")
        String variety,

        @Schema(description = "재배 방식", example = "시설(또는 노지)")
        CultivationType cultivationType,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리 123")
        String cultivationRegion,

        @Schema(description = "재배 면적", example = "1320")
        Integer cultivationArea
) {
    public static UserCropListResponse from(UserCrop userCrop) {
        return UserCropListResponse.builder()
                .name(userCrop.getName())
                .variety(userCrop.getVariety())
                .cultivationType(userCrop.getCultivationType())
                .cultivationRegion(userCrop.getCultivationRegion())
                .cultivationArea(userCrop.getCultivationArea())
                .build();
    }
}
