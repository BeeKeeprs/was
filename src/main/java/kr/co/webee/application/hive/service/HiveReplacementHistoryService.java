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
import java.util.Optional;

import static java.time.temporal.ChronoUnit.*;

@RequiredArgsConstructor
@Service
public class HiveReplacementHistoryService {
    private final HiveRepository hiveRepository;
    private final HiveReplacementHistoryRepository hiveReplacementHistoryRepository;

    @Transactional
    public HiveReplacementHistoryCreateResponse createReplacementHistory(Long hiveId, Long userId, HiveReplacementHistoryCreateRequest request) {
        Hive hive = hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        validateReplacedAt(hiveId, request.replacedAt());

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

        validateUpdateReplacedAt(hiveId, history.getReplacedAt(), request.replacedAt());

        updateOlderUsageDays(hiveId, history.getReplacedAt(), request.replacedAt());
        updateCurrentUsageDays(history, hiveId, history.getReplacedAt(), request.replacedAt());

        history.updateReplacedAt(request.replacedAt());
    }

    @Transactional
    public void deleteReplacementHistory(Long hiveId, Long historyId, Long userId) {
        hiveRepository.findByIdAndUserId(hiveId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_NOT_FOUND));

        HiveReplacementHistory history = hiveReplacementHistoryRepository.findByIdAndHiveId(historyId, hiveId)
                .orElseThrow(() -> new BusinessException(ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND));

        recalculateOnDelete(hiveId, history.getReplacedAt());

        hiveReplacementHistoryRepository.delete(history);
    }

    private void validateReplacedAt(Long hiveId, LocalDate replacedAt) {
        hiveReplacementHistoryRepository.findLatestByHiveId(hiveId)
                .ifPresent(latest -> {
                    if (!replacedAt.isAfter(latest.getReplacedAt())) {
                        throw new BusinessException(ErrorType.HIVE_REPLACEMENT_HISTORY_INVALID_DATE);
                    }
                });
    }

    private void validateUpdateReplacedAt(Long hiveId, LocalDate oldReplacedAt, LocalDate newReplacedAt) {
        hiveReplacementHistoryRepository.findOlderByHiveId(hiveId, oldReplacedAt)
                .ifPresent(older -> {
                    if (!newReplacedAt.isAfter(older.getReplacedAt())) {
                        throw new BusinessException(ErrorType.HIVE_REPLACEMENT_HISTORY_INVALID_DATE);
                    }
                });

        hiveReplacementHistoryRepository.findNewerByHiveId(hiveId, oldReplacedAt)
                .ifPresent(newer -> {
                    if (!newReplacedAt.isBefore(newer.getReplacedAt())) {
                        throw new BusinessException(ErrorType.HIVE_REPLACEMENT_HISTORY_INVALID_DATE);
                    }
                });
    }

    private void updatePreviousUsageDays(Long hiveId, HiveReplacementHistoryCreateRequest request) {
        hiveReplacementHistoryRepository.findLatestByHiveId(hiveId)
                .ifPresent(prev -> prev.updateUsageDays(DAYS.between(prev.getReplacedAt(), request.replacedAt())));
    }

    private void updateOlderUsageDays(Long hiveId, LocalDate oldReplacedAt, LocalDate newReplacedAt) {
        hiveReplacementHistoryRepository.findOlderByHiveId(hiveId, oldReplacedAt)
                .ifPresent(older -> older.updateUsageDays(DAYS.between(older.getReplacedAt(), newReplacedAt)));
    }

    private void updateCurrentUsageDays(HiveReplacementHistory history, Long hiveId, LocalDate oldReplacedAt, LocalDate newReplacedAt) {
        hiveReplacementHistoryRepository.findNewerByHiveId(hiveId, oldReplacedAt)
                .ifPresent(newer -> history.updateUsageDays(DAYS.between(newReplacedAt, newer.getReplacedAt())));
    }

    private void recalculateOnDelete(Long hiveId, LocalDate deletedReplacedAt) {
        Optional<HiveReplacementHistory> newerOpt = hiveReplacementHistoryRepository.findNewerByHiveId(hiveId, deletedReplacedAt);

        if (newerOpt.isPresent()) {
            // 삭제 대상이 중간 레코드
            HiveReplacementHistory newer = newerOpt.get();

            hiveReplacementHistoryRepository.findOlderByHiveId(hiveId, deletedReplacedAt)
                    .ifPresent(older -> older.updateUsageDays(DAYS.between(older.getReplacedAt(), newer.getReplacedAt())));
        } else {
            // 삭제 대상이 최신 레코드
            hiveReplacementHistoryRepository.findOlderByHiveId(hiveId, deletedReplacedAt)
                    .ifPresent(HiveReplacementHistory::clearUsageDays);
        }
    }
}
