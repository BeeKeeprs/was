package kr.co.webee.presentation.profile.crop.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자 재배 작물 정보 ID 값")
public record UserCropCreateResponse(
        @Schema(description = "사용자 재배 작물 정보 ID", example = "1")
        Long userCropId
) {
    public static UserCropCreateResponse of(Long userCropId) {
        return UserCropCreateResponse.builder()
                .userCropId(userCropId)
                .build();
    }
}
