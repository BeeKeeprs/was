package kr.co.webee.presentation.fcmtoken.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.fcmtoken.entity.FcmToken;
import kr.co.webee.domain.user.entity.User;

public record FcmTokenRegisterRequest(
        @NotBlank
        String token,

        @NotBlank
        String deviceInfo
) {
    public FcmToken toEntity(User user) {
        return FcmToken.builder()
                .token(token)
                .deviceInfo(deviceInfo)
                .user(user)
                .build();
    }
}
