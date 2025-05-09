package kr.co.webee.presentation.profile.crop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.profile.crop.entity.UserCrop;
import kr.co.webee.domain.profile.crop.type.CultivationType;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.infrastructure.geocoding.dto.CoordinatesDto;

import java.time.LocalDate;

@Schema(description = "사용자 재배 작물 정보 request")
public record UserCropRequest(
        @Schema(description = "작물명", example = "딸기")
        @NotBlank
        String name,

        @Schema(description = "품종", example = "설향")
        String variety,

        @Schema(description = "재배 방식", example = "CONTROLLED", examples = {"CONTROLLED", "OPEN_FIELD"})
        @NotNull
        CultivationType cultivationType,

        @Schema(description = "재배 지역", example = "충청남도 논산시 연무읍 봉동리 123")
        @NotBlank
        String cultivationRegion,

        @Schema(description = "재배 면적", example = "1320")
        @NotNull
        Integer cultivationArea,

        @Schema(description = "정식(또는 파종)일", example = "2024-02-25")
        @NotNull
        LocalDate plantingDate
) {
        public UserCrop toEntity(CoordinatesDto coordinatesDto, User user) {
                return UserCrop.builder()
                        .name(name)
                        .variety(variety)
                        .cultivationType(cultivationType)
                        .cultivationRegion(cultivationRegion)
                        .latitude(coordinatesDto.latitude())
                        .longitude(coordinatesDto.longitude())
                        .cultivationArea(cultivationArea)
                        .plantingDate(plantingDate)
                        .user(user)
                        .build();
        }
}
