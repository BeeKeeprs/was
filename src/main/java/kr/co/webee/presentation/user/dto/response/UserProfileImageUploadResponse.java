package kr.co.webee.presentation.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "프로필 이미지 업로드 응답")
public record UserProfileImageUploadResponse(
        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/users/1/profile/1713912365123_profile.png")
        String profileImageUrl
) {
    public static UserProfileImageUploadResponse of(String profileImageUrl) {
        return UserProfileImageUploadResponse.builder()
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
