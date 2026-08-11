package kr.co.webee.application.hive.service;

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
import kr.co.webee.domain.hive.type.ControlType;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.infrastructure.mqtt.config.MqttBrokerConfig;
import kr.co.webee.infrastructure.redis.service.RedisService;
import kr.co.webee.presentation.hive.dto.request.HiveManualControlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveControlService {
    private static final Duration COMMAND_TTL = Duration.ofSeconds(30);

    private final HiveControlRepository hiveControlRepository;
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
    public void setManualControl(Long hiveId, Long userId, HiveManualControlRequest request) {
        // 1. 벌통 조회
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        // 2. redis에 command 정보 저장
        String commandId = UUID.randomUUID().toString();

        redisService.set(
                HivePendingCommand.redisKey(commandId),
                jsonConverter.toJson(HivePendingCommand.of(userId, hiveId, request.targetTemperature(), request.targetHumidity())),
                COMMAND_TTL
        );

        // 3. MQTT 발송
        HiveManualControlCommandRequest command = HiveManualControlCommandRequest.of(commandId, request.targetTemperature(), request.targetHumidity(), hive.getMacAddress());
        mqttPublisher.publish("hive/%s/control".formatted(hive.getMacAddress()), jsonConverter.toJson(command));
    }

    @Transactional
    public HiveControlCommandProcessResponse processManualControlCommandResponse(HivePendingCommand pending, HiveControlCommandResponse response) {
        if (!response.success()) {
            log.warn("제어 명령 실패 commandId={}, message={}", response.commandId(), response.message());
            return HiveControlCommandProcessResponse.failure(response.commandId(), pending.hiveId(), response.message());
        }

        Hive hive = hiveRepository.findById(pending.hiveId())
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        if (pending.targetTemperature() != null) {
            hiveControlRepository.findByHiveIdAndType(pending.hiveId(), ControlType.TEMPERATURE)
                    .ifPresentOrElse(
                            control -> control.updateTargetValue(pending.targetTemperature()),
                            () -> {
                                HiveControl control = hiveControlRepository.save(HiveControl.create(hive, ControlType.TEMPERATURE));
                                control.updateTargetValue(pending.targetTemperature());
                            }
                    );
        }

        if (pending.targetHumidity() != null) {
            hiveControlRepository.findByHiveIdAndType(pending.hiveId(), ControlType.HUMIDITY)
                    .ifPresentOrElse(
                            control -> control.updateTargetValue(pending.targetHumidity()),
                            () -> {
                                HiveControl control = hiveControlRepository.save(HiveControl.create(hive, ControlType.HUMIDITY));
                                control.updateTargetValue(pending.targetHumidity());
                            }
                    );
        }

        return HiveControlCommandProcessResponse.success(response.commandId(), pending.hiveId(), pending.targetTemperature(), pending.targetHumidity());
    }
}
