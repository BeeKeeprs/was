package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveControlCommandRequest;
import kr.co.webee.application.hive.dto.response.HiveControlCommandResponse;
import kr.co.webee.application.hive.dto.response.HiveAutoControlCommandProcessResponse;
import kr.co.webee.application.hive.dto.HivePendingCommand;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.common.util.JsonConverter;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.repository.HiveControlRepository;
import kr.co.webee.domain.hive.repository.HiveControlScheduleRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.type.ControlMode;
import kr.co.webee.infrastructure.mqtt.config.MqttBrokerConfig;
import kr.co.webee.infrastructure.redis.service.RedisService;
import kr.co.webee.presentation.hive.dto.request.HiveAutoControlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveControlService {
    private static final Duration COMMAND_TTL = Duration.ofSeconds(30);

    private final HiveControlRepository hiveControlRepository;
    private final HiveControlScheduleRepository hiveControlScheduleRepository;
    private final HiveRepository hiveRepository;
    private final RedisService redisService;
    private final JsonConverter jsonConverter;
    private final MqttBrokerConfig.MqttPublisher mqttPublisher;

    @Transactional(readOnly = true)
    public void setAutoControl(Long hiveId, Long userId, HiveAutoControlRequest request) {
        // 1. 벌통 조회
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        // 2. 벌통 제어 조회
        HiveControl existing = hiveControlRepository.findByHiveIdAndType(hiveId, request.type()).orElse(null);

        // 3. 기존의 수동/자동제어 스케줄 충돌 확인
        validateManualEnabled(existing);
        validateActiveSchedule(hiveId);

        // 4. MQTT 응답 수신 시 command 구분용 UUID 생성
        String commandId = UUID.randomUUID().toString();

        // 5. redis에 command 정보 저장
        redisService.set(
                HivePendingCommand.redisKey(commandId),
                HivePendingCommand.of(userId, hiveId, request.type(), request.enabled()),
                COMMAND_TTL
        );

        // 6. MQTT 발송
        HiveControlCommandRequest command = HiveControlCommandRequest.of(commandId, request.type(), ControlMode.AUTO, request.enabled(), hive.getMacAddress());
        mqttPublisher.publish("hive/%s/control".formatted(hive.getMacAddress()), jsonConverter.toJson(command));
    }

    @Transactional
    public HiveAutoControlCommandProcessResponse processAutoControlCommandResponse(HivePendingCommand pending, HiveControlCommandResponse response) {
        if (!response.success()) {
            log.warn("자동제어 명령 실패 commandId={}, message={}", response.commandId(), response.message());

            return HiveAutoControlCommandProcessResponse.failure(response.commandId(), pending.hiveId(), pending.type(), response.message());
        }

        Hive hive = hiveRepository.findById(pending.hiveId())
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        hiveControlRepository.findByHiveIdAndType(pending.hiveId(), pending.type())
                .ifPresentOrElse(
                        control -> control.updateAutoEnabled(pending.autoEnabled()),

                        () -> hiveControlRepository.save(
                                HiveControl.createAutoControl(hive, pending.type(), pending.autoEnabled())
                        ));

        return HiveAutoControlCommandProcessResponse.success(response.commandId(), pending.hiveId(), pending.type(), pending.autoEnabled());
    }

    private static void validateManualEnabled(HiveControl existing) {
        if (existing != null && existing.isManualEnabled()) { // 수동 제어가 활성화되있는지
            throw new BusinessException(ErrorType.HIVE_AUTO_CONTROL_BLOCKED_BY_MANUAL);
        }
    }

    private void validateActiveSchedule(Long hiveId) { // 현재 기점으로 자동제어 스케줄이 진행중인지
        if (hiveControlScheduleRepository.existsActiveSchedule(hiveId, LocalTime.now())) {
            throw new BusinessException(ErrorType.HIVE_AUTO_CONTROL_BLOCKED_BY_SCHEDULE);
        }
    }
}
