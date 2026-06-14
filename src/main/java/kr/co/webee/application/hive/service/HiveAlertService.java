package kr.co.webee.application.hive.service;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import kr.co.webee.application.hive.dto.request.HiveAlertRequest;
import kr.co.webee.domain.fcmtoken.repository.FcmTokenRepository;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.notification.entity.Notification;
import kr.co.webee.domain.notification.repository.NotificationRepository;
import kr.co.webee.infrastructure.fcm.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

import static kr.co.webee.domain.notification.type.NotificationType.HIVE_ALERT;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveAlertService {
    private final HiveRepository hiveRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationRepository notificationRepository;
    private final FcmNotificationService fcmNotificationService;
    private final MeterRegistry meterRegistry;

    @Transactional
    public void processAlert(HiveAlertRequest request, String macAddress) {
        Timer.Sample sample = Timer.start(meterRegistry);

        Hive hive = hiveRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 벌통입니다. macAddress=" + macAddress));

        String title = HIVE_ALERT.buildHiveAlertTitle(hive.getName(), request.type());
        String content = HIVE_ALERT.buildHiveAlertContent(hive.getName(), request.type(), request.value(), request.threshold());

        Notification notification = Notification.create(hive.getUser(), HIVE_ALERT, title, content);
        notificationRepository.save(notification);

        fcmTokenRepository.findAllByUserId(hive.getUser().getId())
                .forEach(fcmToken -> {
                    fcmNotificationService.send(fcmToken.getToken(), title, content);
                });

        sample.stop(Timer.builder("hive.alert.process.duration")
                .publishPercentileHistogram()
                .register(meterRegistry));

        long e2eLatencyMs = Duration.between(request.timestamp(), Instant.now()).toMillis();
        DistributionSummary.builder("hive.alert.e2e.latency")
                .baseUnit("ms")
                .publishPercentileHistogram()
                .register(meterRegistry)
                .record(e2eLatencyMs);
    }
}
