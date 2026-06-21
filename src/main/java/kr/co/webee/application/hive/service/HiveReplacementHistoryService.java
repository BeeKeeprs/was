package kr.co.webee.application.hive.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveReplacementHistory;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.repository.HiveReplacementHistoryRepository;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryCreateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryCreateResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class HiveReplacementHistoryService {
    private final HiveRepository hiveRepository;
    private final HiveReplacementHistoryRepository hiveReplacementHistoryRepository;

    @Transactional
    public HiveReplacementHistoryCreateResponse createReplacementHistory(Long hiveId, Long userId, HiveReplacementHistoryCreateRequest request) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        // 직전 기록의 usageDays 업데이트
        hiveReplacementHistoryRepository.findLatestByHiveId(hiveId)
                .ifPresent(prev -> prev.updateUsageDays(
                        ChronoUnit.DAYS.between(prev.getReplacedAt(), request.replacedAt())
                ));

        HiveReplacementHistory history = hiveReplacementHistoryRepository.save(request.toEntity(hive));

        return HiveReplacementHistoryCreateResponse.of(history.getId());
    }
}
