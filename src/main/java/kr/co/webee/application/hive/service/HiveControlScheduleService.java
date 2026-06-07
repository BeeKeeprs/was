package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveControlScheduleRegisterRequest;
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

@RequiredArgsConstructor
@Service
public class HiveControlScheduleService {
    private final HiveControlScheduleRepository hiveControlScheduleRepository;
    private final HiveRepository hiveRepository;

    @Transactional
    public HiveControlScheduleRegisterResponse registerHiveControlSchedule(Long hiveId, Long userId, HiveControlScheduleRegisterRequest request) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        HiveControlSchedule hiveControlSchedule = hiveControlScheduleRepository.save(request.toEntity(hive));

        return HiveControlScheduleRegisterResponse.of(hiveControlSchedule.getId());
    }
}
