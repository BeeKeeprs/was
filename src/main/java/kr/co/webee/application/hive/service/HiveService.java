package kr.co.webee.application.hive.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.repository.HiveBeeCountRepository;
import kr.co.webee.domain.hive.repository.HiveControlRepository;
import kr.co.webee.domain.hive.repository.HiveControlScheduleRepository;
import kr.co.webee.domain.hive.repository.HiveGateActionRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.repository.HiveReplacementHistoryRepository;
import kr.co.webee.domain.hive.repository.HiveTelemetryRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.hive.dto.request.HiveRegisterRequest;
import kr.co.webee.presentation.hive.dto.request.HiveUpdateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveDetailResponse;
import kr.co.webee.presentation.hive.dto.response.HiveListResponse;
import kr.co.webee.presentation.hive.dto.response.HiveRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HiveService {
    private final HiveRepository hiveRepository;
    private final UserRepository userRepository;
    private final HiveTelemetryRepository hiveTelemetryRepository;
    private final HiveBeeCountRepository hiveBeeCountRepository;
    private final HiveControlRepository hiveControlRepository;
    private final HiveControlScheduleRepository hiveControlScheduleRepository;
    private final HiveGateActionRepository hiveGateActionRepository;
    private final HiveReplacementHistoryRepository hiveReplacementHistoryRepository;

    @Transactional
    public HiveRegisterResponse registerHive(HiveRegisterRequest request, Long userId) {
        if (hiveRepository.existsByMacAddress(request.macAddress())) {
            throw new BusinessException(ErrorType.HIVE_MAC_ADDRESS_ALREADY_EXISTS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENTITY_NOT_FOUND));

        Hive hive = hiveRepository.save(request.toEntity(user));

        return HiveRegisterResponse.of(hive.getId());
    }

    @Transactional(readOnly = true)
    public HiveListResponse getAllHives(Long userId) {
        List<Hive> hives = hiveRepository.findByUserId(userId);
        return HiveListResponse.from(hives);
    }

    @Transactional(readOnly = true)
    public HiveDetailResponse getHiveDetail(Long hiveId, Long userId) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        return HiveDetailResponse.from(hive);
    }

    @Transactional
    public void updateHive(Long hiveId, Long userId, HiveUpdateRequest request) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        hive.update(request.name(), request.region(), request.location(), request.memo());
    }

    @Transactional
    public void deleteHive(Long hiveId, Long userId) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        hiveTelemetryRepository.deleteAllByHiveId(hiveId);
        hiveBeeCountRepository.deleteAllByHiveId(hiveId);
        hiveControlRepository.deleteAllByHiveId(hiveId);
        hiveControlScheduleRepository.deleteAllByHiveId(hiveId);
        hiveGateActionRepository.deleteAllByHiveId(hiveId);
        hiveReplacementHistoryRepository.deleteAllByHiveId(hiveId);
        hiveRepository.delete(hive);
    }
}
