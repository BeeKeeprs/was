package kr.co.webee.application.hive.dto;

import lombok.Builder;

@Builder
public record FcmMessageDto(
        String fcmToken,
        String title,
        String content
) {
    public static FcmMessageDto of(String fcmToken, String title, String content) {
        return FcmMessageDto.builder()
                .fcmToken(fcmToken)
                .title(title)
                .content(content)
                .build();
    }
}
