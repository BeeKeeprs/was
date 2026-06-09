package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveControlScheduleRegisterRequest;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleListResponse;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleRegisterResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import kr.co.webee.domain.hive.repository.HiveControlScheduleRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HiveControlScheduleService {
    private final HiveControlScheduleRepository hiveControlScheduleRepository;
    private final HiveRepository hiveRepository;

    @Transactional
    public HiveControlScheduleRegisterResponse registerHiveControlSchedule(Long hiveId, Long userId, HiveControlScheduleRegisterRequest request) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        validateScheduleTimeRange(request);
        validateAlreadyExistsSchedule(hiveId, request);

        HiveControlSchedule hiveControlSchedule = hiveControlScheduleRepository.save(request.toEntity(hive));

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
}
