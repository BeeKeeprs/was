package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateTunnelAlertRequest;
import kr.co.webee.application.hive.dto.FcmMessageDto;
import kr.co.webee.domain.fcmtoken.repository.FcmTokenRepository;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.notification.entity.Notification;
import kr.co.webee.domain.notification.repository.NotificationRepository;
import kr.co.webee.infrastructure.rabbitmq.RabbitMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.webee.domain.notification.type.NotificationType.GATE_TUNNEL_ALERT;

@Slf4j
@RequiredArgsConstructor
@Service
public class GateTunnelAlertService {
    private final GateRepository gateRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationRepository notificationRepository;
    private final RabbitMessageProducer rabbitMessageProducer;

    @Transactional
    public void processAlert(GateTunnelAlertRequest request, String macAddress) {
        Gate gate = gateRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 개폐기입니다. macAddress=" + macAddress));

        String title = GATE_TUNNEL_ALERT.buildGateTunnelAlertTitle(gate.getName());
        String content = GATE_TUNNEL_ALERT.buildGateTunnelAlertContent(
                gate.getName(), request.tunnel(), request.sensor(), request.blockedSeconds());

        notificationRepository.save(Notification.create(gate.getUser(), GATE_TUNNEL_ALERT, title, content));

        fcmTokenRepository.findAllByUserId(gate.getUser().getId())
                .forEach(fcmToken -> rabbitMessageProducer.send(FcmMessageDto.of(fcmToken.getToken(), title, content)));
    }
}
