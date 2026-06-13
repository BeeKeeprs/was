package kr.co.webee.infrastructure.fcm;

import com.google.firebase.messaging.*;
import kr.co.webee.application.hive.dto.FcmMessageDto;
import kr.co.webee.infrastructure.fcm.dto.FcmSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmNotificationService {

    public FcmSendResponse send(FcmMessageDto fcmMessageDto) {
        Message message = buildMessage(fcmMessageDto.fcmToken(), fcmMessageDto.title(), fcmMessageDto.content());

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("fcm 전송 성공");
            return FcmSendResponse.success(response);
        } catch (FirebaseMessagingException e) {

            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                log.error("FCM 유효하지 않은 토큰으로 인한 전송 실패. {}", e.getMessage());
                return FcmSendResponse.invalidToken(e.getMessage());
            }

            log.error("FCM 알림 전송 실패. {}", e.getMessage());
            return FcmSendResponse.failure(e.getMessage());
        }
    }

    private Message buildMessage(String token, String title, String body) {
        return Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();
    }
}
