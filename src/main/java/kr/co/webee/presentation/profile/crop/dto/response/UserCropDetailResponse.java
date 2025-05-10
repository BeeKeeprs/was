package kr.co.webee.presentation.profile.crop.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "사용자 재배 작물 상세 정보")
public record UserCropDetailResponse(
        @Schema(description = "작물ID", example = "1")
        Long id,

        @Schema(description = "작물명", example = "딸기")
        String name,

        @Schema(description = "품종", example = "설향")
        String variety,

        @Schema(description = "재배 방식", example = "CONTROLLED", examples = {"CONTROLLED", "OPEN_FIELD"})
        CultivationType cultivationType,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리 123")
        String cultivationRegion,

        @Schema(description = "재배 면적", example = "1320")
        Integer cultivationArea,

        @Schema(description = "정식(또는 파종)일", example = "2024-02-25")
        LocalDate plantingDate
) {
    public static UserCropDetailResponse from(UserCrop userCrop) {
        return UserCropDetailResponse.builder()
                .id(userCrop.getId())
                .name(userCrop.getName())
                .variety(userCrop.getVariety())
                .cultivationType(userCrop.getCultivationType())
                .cultivationRegion(userCrop.getCultivationRegion())
                .cultivationArea(userCrop.getCultivationArea())
                .plantingDate(userCrop.getPlantingDate())
                .build();
    }
}
