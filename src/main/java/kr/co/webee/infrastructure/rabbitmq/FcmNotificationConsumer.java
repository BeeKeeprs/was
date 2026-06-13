package kr.co.webee.infrastructure.rabbitmq;

import kr.co.webee.application.hive.dto.FcmMessageDto;
import kr.co.webee.domain.fcmtoken.repository.FcmTokenRepository;
import kr.co.webee.infrastructure.fcm.FcmNotificationService;
import kr.co.webee.infrastructure.fcm.dto.FcmSendResponse;
import kr.co.webee.infrastructure.fcm.exception.FcmSendFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FcmNotificationConsumer {
    private final FcmNotificationService fcmNotificationService;
    private final FcmTokenRepository fcmTokenRepository;

    @RabbitListener(queues = "${rabbitmq.queue-name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(FcmMessageDto message) {
        FcmSendResponse response = fcmNotificationService.send(message);

        if (response.shouldRemoveToken()) {
            fcmTokenRepository.deleteByToken(message.fcmToken());
            return;
        }

        if (!response.success()) {
            throw new FcmSendFailureException(response.message());
        }
    }
}
