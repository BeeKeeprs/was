package kr.co.webee.application.hive.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveReplacementHistory;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.repository.HiveReplacementHistoryRepository;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryCreateRequest;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryUpdateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryCreateResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryDetailResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryListResponse;
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

        updatePreviousUsageDays(hiveId, request);

        HiveReplacementHistory history = hiveReplacementHistoryRepository.save(request.toEntity(hive));

        return HiveReplacementHistoryCreateResponse.of(history.getId());
    }

    @Transactional(readOnly = true)
    public HiveReplacementHistoryDetailResponse getReplacementHistoryDetail(Long hiveId, Long historyId, Long userId) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        HiveReplacementHistory history = hiveReplacementHistoryRepository.findByIdAndHiveId(historyId, hiveId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND));

        return HiveReplacementHistoryDetailResponse.from(history);
    }

    @Transactional(readOnly = true)
    public Slice<HiveReplacementHistoryListResponse> getAllReplacementHistories(Long hiveId, Long userId, Pageable pageable) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        return hiveReplacementHistoryRepository.findAllByHiveId(hiveId, pageable)
                .map(HiveReplacementHistoryListResponse::from);
    }

    @Transactional
    public void updateReplacementHistory(Long hiveId, Long historyId, Long userId, HiveReplacementHistoryUpdateRequest request) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        HiveReplacementHistory history = hiveReplacementHistoryRepository.findByIdAndHiveId(historyId, hiveId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND));

        updateNewerUsageDays(hiveId, history.getReplacedAt(), request.replacedAt());
        updateCurrentUsageDays(history, hiveId, history.getReplacedAt(), request.replacedAt());

        history.updateReplacedAt(request.replacedAt());
    }

    private void updatePreviousUsageDays(Long hiveId, HiveReplacementHistoryCreateRequest request) {
        hiveReplacementHistoryRepository.findLatestByHiveId(hiveId)
                .ifPresent(prev -> prev.updateUsageDays(
                        ChronoUnit.DAYS.between(prev.getReplacedAt(), request.replacedAt())
                ));
    }

    private void updateNewerUsageDays(Long hiveId, LocalDate oldReplacedAt, LocalDate newReplacedAt) {
        hiveReplacementHistoryRepository.findNewerByHiveId(hiveId, oldReplacedAt)
                .ifPresent(newer -> newer.updateUsageDays(
                        ChronoUnit.DAYS.between(newReplacedAt, newer.getReplacedAt())
                ));
    }

    private void updateCurrentUsageDays(HiveReplacementHistory history, Long hiveId, LocalDate oldReplacedAt, LocalDate newReplacedAt) {
        hiveReplacementHistoryRepository.findOlderByHiveId(hiveId, oldReplacedAt)
                .ifPresentOrElse(
                        older -> history.updateUsageDays(ChronoUnit.DAYS.between(older.getReplacedAt(), newReplacedAt)),
                        () -> history.updateUsageDays(ChronoUnit.DAYS.between(newReplacedAt, LocalDate.now()))
                );
    }
}
