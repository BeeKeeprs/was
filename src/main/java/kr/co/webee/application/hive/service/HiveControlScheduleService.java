package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.HiveControlScheduleRegisterEvent;
import kr.co.webee.application.hive.dto.HivePendingCommand;
import kr.co.webee.application.hive.dto.request.HiveAutoControlCommandRequest;
import kr.co.webee.application.hive.dto.request.HiveControlScheduleRegisterRequest;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleListResponse;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleRegisterResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.common.util.JsonConverter;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import kr.co.webee.domain.hive.repository.HiveControlRepository;
import kr.co.webee.domain.hive.repository.HiveControlScheduleRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.type.ControlType;
import kr.co.webee.infrastructure.mqtt.config.MqttBrokerConfig;
import kr.co.webee.infrastructure.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveControlScheduleService {
    private static final Duration COMMAND_TTL = Duration.ofSeconds(30);

    private final HiveControlScheduleRepository hiveControlScheduleRepository;
    private final HiveControlRepository hiveControlRepository;
    private final HiveRepository hiveRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final HiveControlScheduleTaskRegistry taskRegistry;
    private final RedisService redisService;
    private final JsonConverter jsonConverter;
    private final MqttBrokerConfig.MqttPublisher mqttPublisher;

    @Transactional
    public HiveControlScheduleRegisterResponse registerHiveControlSchedule(Long hiveId, Long userId, HiveControlScheduleRegisterRequest request) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        validateScheduleTimeRange(request);
        validateAlreadyExistsSchedule(hiveId, request);

        HiveControlSchedule hiveControlSchedule = hiveControlScheduleRepository.save(request.toEntity(hive));

        eventPublisher.publishEvent(HiveControlScheduleRegisterEvent.from(hiveControlSchedule));

        return HiveControlScheduleRegisterResponse.of(hiveControlSchedule.getId());
    }

    @Transactional(readOnly = true)
    public List<HiveControlScheduleListResponse> getHiveControlScheduleList(Long hiveId, Long userId) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        return hiveControlScheduleRepository.findAllByHiveId(hiveId).stream()
                .map(HiveControlScheduleListResponse::from)
                .toList();
    }

    @Transactional
    public void deleteHiveControlSchedule(Long hiveId, Long userId, Long scheduleId) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        HiveControlSchedule schedule = hiveControlScheduleRepository.findByIdAndHiveId(scheduleId, hiveId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_CONTROL_SCHEDULE_NOT_FOUND));

        hiveControlScheduleRepository.delete(schedule);
        taskRegistry.cancel(scheduleId);
    }

    @Transactional(readOnly = true)
    public void publishAutoControlCommand(Long scheduleId, boolean enabled) {
        HiveControlSchedule schedule = hiveControlScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_CONTROL_SCHEDULE_NOT_FOUND));

        Hive hive = schedule.getHive();
        Long hiveId = hive.getId();
        Long userId = hive.getUser().getId();

        Set<ControlType> manualEnabledTypes = getManualEnabledTypes(hiveId);

        for (ControlType type : ControlType.autoControlTypes()) {
            // 수동제어 활성화 중인 센서는 건너뜀
            if (manualEnabledTypes.contains(type)) {
                log.warn("수동제어 활성화로 인해 자동제어 스케줄 스킵. hiveId={}, type={}", hiveId, type);
                continue;
            }

            String commandId = UUID.randomUUID().toString();

            redisService.set(
                    HivePendingCommand.redisKey(commandId),
                    HivePendingCommand.createAutoControlCommand(userId, hiveId, type, enabled),
                    COMMAND_TTL
            );

            HiveAutoControlCommandRequest command = HiveAutoControlCommandRequest.of(commandId, type, enabled, hive.getMacAddress());
            mqttPublisher.publish("hive/%s/control".formatted(hive.getMacAddress()), jsonConverter.toJson(command));
        }

        // endTime 실행 시에만 익일 스케줄 재등록
        if (!enabled) {
            eventPublisher.publishEvent(HiveControlScheduleRegisterEvent.from(schedule));
        }
    }

    @Transactional(readOnly = true)
    public List<HiveControlSchedule> findAllEnabledSchedules() {
        return hiveControlScheduleRepository.findAllByEnabled(true);
    }

    private static void validateScheduleTimeRange(HiveControlScheduleRegisterRequest request) {
        if (!request.isValidTimeRange()) {
            throw new BusinessException(ErrorType.HIVE_CONTROL_SCHEDULE_INVALID_TIME_RANGE);
        }
    }

    private void validateAlreadyExistsSchedule(Long hiveId, HiveControlScheduleRegisterRequest request) {
        List<HiveControlSchedule> existingSchedules = hiveControlScheduleRepository.findAllByHiveId(hiveId);

        boolean hasOverlap = existingSchedules.stream()
                .anyMatch(existing ->
                        request.startTime().isBefore(existing.getEndTime())
                                && request.endTime().isAfter(existing.getStartTime())
                );

        if (hasOverlap) {
            throw new BusinessException(ErrorType.HIVE_CONTROL_SCHEDULE_ALREADY_EXISTS_IN_TIME_RANGE);
        }
    }

    private Set<ControlType> getManualEnabledTypes(Long hiveId) {
        return hiveControlRepository.findAllByHiveId(hiveId).stream()
                .filter(HiveControl::isManualEnabled)
                .map(HiveControl::getType)
                .collect(Collectors.toSet());
    }
}
