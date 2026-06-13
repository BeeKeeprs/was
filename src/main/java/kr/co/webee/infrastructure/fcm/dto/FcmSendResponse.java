package kr.co.webee.infrastructure.fcm.dto;

import lombok.Builder;

@Builder
public record FcmSendResponse(
        boolean success,
        String message,
        boolean shouldRemoveToken
) {
    public static FcmSendResponse success(String message) {
        return FcmSendResponse.builder()
                .success(true)
                .message(message)
                .shouldRemoveToken(false)
                .build();
    }

    public static FcmSendResponse failure(String message) {
        return FcmSendResponse.builder()
                .success(false)
                .message(message)
                .shouldRemoveToken(false)
                .build();
    }

    public static FcmSendResponse invalidToken(String message) {
        return FcmSendResponse.builder()
                .success(false)
                .message(message)
                .shouldRemoveToken(true)
                .build();
    }
}
