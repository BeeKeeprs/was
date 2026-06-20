package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveAutoControlCommandRequest;
import kr.co.webee.application.hive.dto.request.HiveManualControlCommandRequest;
import kr.co.webee.application.hive.dto.response.HiveControlCommandResponse;
import kr.co.webee.application.hive.dto.response.HiveControlCommandProcessResponse;
import kr.co.webee.application.hive.dto.response.HiveControlListResponse;
import kr.co.webee.application.hive.dto.HivePendingCommand;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.common.util.JsonConverter;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.repository.HiveControlRepository;
import kr.co.webee.domain.hive.repository.HiveControlScheduleRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.infrastructure.mqtt.config.MqttBrokerConfig;
import kr.co.webee.infrastructure.redis.service.RedisService;
import kr.co.webee.presentation.hive.dto.request.HiveAutoControlRequest;
import kr.co.webee.presentation.hive.dto.request.HiveManualControlRequest;
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
    public HiveControlListResponse getControlList(Long hiveId, Long userId) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        return HiveControlListResponse.of(hiveControlRepository.findAllByHiveId(hiveId));
    }

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
                jsonConverter.toJson(HivePendingCommand.createAutoControlCommand(userId, hiveId, request.type(), request.enabled())),
                COMMAND_TTL
        );

        // 6. MQTT 발송
        HiveAutoControlCommandRequest command = HiveAutoControlCommandRequest.of(commandId, request.type(), request.enabled(), hive.getMacAddress());
        mqttPublisher.publish("hive/%s/control".formatted(hive.getMacAddress()), jsonConverter.toJson(command));
    }

    @Transactional(readOnly = true)
    public void setManualControl(Long hiveId, Long userId, HiveManualControlRequest request) {
        // 1. 벌통 조회
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        // 2. MQTT 응답 수신 시 command 구분용 UUID 생성
        String commandId = UUID.randomUUID().toString();

        // 3. redis에 command 정보 저장
        redisService.set(
                HivePendingCommand.redisKey(commandId),
                jsonConverter.toJson(HivePendingCommand.createManualControlCommand(userId, hiveId, request.type(), request.enabled(), request.isOn())),
                COMMAND_TTL
        );

        // 4. MQTT 발송
        HiveManualControlCommandRequest command = HiveManualControlCommandRequest.of(commandId, request.type(), request.enabled(), request.isOn(), hive.getMacAddress());
        mqttPublisher.publish("hive/%s/control".formatted(hive.getMacAddress()), jsonConverter.toJson(command));
    }

    @Transactional
    public HiveControlCommandProcessResponse processAutoControlCommandResponse(HivePendingCommand pending, HiveControlCommandResponse response) {
        // 요청 실패 처리
        if (!response.success()) {
            log.warn("자동제어 명령 실패 commandId={}, message={}", response.commandId(), response.message());
            return HiveControlCommandProcessResponse.failure(response.commandId(), pending.hiveId(), pending.type(), response.message());
        }

        // 자동 제어 활성화 처리
        Hive hive = hiveRepository.findById(pending.hiveId())
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        hiveControlRepository.findByHiveIdAndType(pending.hiveId(), pending.type())
                .ifPresentOrElse(
                        control -> control.updateAutoControl(pending.autoEnabled()),
                        () -> hiveControlRepository.save(
                                HiveControl.createAutoControl(hive, pending.type(), pending.autoEnabled())
                        ));

        return HiveControlCommandProcessResponse.autoSuccess(response.commandId(), pending.hiveId(), pending.type(), pending.autoEnabled());
    }

    @Transactional
    public HiveControlCommandProcessResponse processManualControlCommandResponse(HivePendingCommand pending, HiveControlCommandResponse response) {
        // 요청 실패 처리
        if (!response.success()) {
            log.warn("수동제어 명령 실패 commandId={}, message={}", response.commandId(), response.message());
            return HiveControlCommandProcessResponse.failure(response.commandId(), pending.hiveId(), pending.type(), response.message());
        }

        // 수동 제어 비활성화 처리
        if (!pending.manualEnabled()) {
            hiveControlRepository.findByHiveIdAndType(pending.hiveId(), pending.type())
                    .ifPresent(HiveControl::disableManualControl);

            return HiveControlCommandProcessResponse.manualSuccess(response.commandId(), pending.hiveId(), pending.type(), false, null);
        }

        // 수동 제어 활성화 처리
        Hive hive = hiveRepository.findById(pending.hiveId())
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        hiveControlRepository.findByHiveIdAndType(pending.hiveId(), pending.type())
                .ifPresentOrElse(
                        control -> control.enableManualControl(pending.isOn()),

                        () -> hiveControlRepository.save(
                                HiveControl.createManualControl(hive, pending.type(), pending.isOn())
                        ));

        return HiveControlCommandProcessResponse.manualSuccess(response.commandId(), pending.hiveId(), pending.type(), true, pending.isOn());
    }

    private static void validateManualEnabled(HiveControl existing) {
        if (existing != null && existing.isManualEnabled()) {
            throw new BusinessException(ErrorType.HIVE_AUTO_CONTROL_BLOCKED_BY_MANUAL);
        }
    }

    private void validateActiveSchedule(Long hiveId) {
        if (hiveControlScheduleRepository.existsActiveSchedule(hiveId, LocalTime.now())) {
            throw new BusinessException(ErrorType.HIVE_AUTO_CONTROL_BLOCKED_BY_SCHEDULE);
        }
    }
}
