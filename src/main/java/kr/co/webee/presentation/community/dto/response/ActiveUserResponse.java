package kr.co.webee.presentation.community.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "활동 중인 유저 정보")
public record ActiveUserResponse(

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String profileImageUrl
) {
    public static ActiveUserResponse from(User user) {
        return ActiveUserResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
