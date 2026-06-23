package kr.co.webee.application.hive.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveReplacementHistory;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.repository.HiveReplacementHistoryRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryCreateRequest;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryUpdateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryCreateResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryDetailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class HiveReplacementHistoryServiceTest {

    @Autowired
    private HiveReplacementHistoryService hiveReplacementHistoryService;

    @Autowired
    private HiveReplacementHistoryRepository hiveReplacementHistoryRepository;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Hive hive;

    @BeforeEach
    void setUp() {
        hiveReplacementHistoryRepository.deleteAllInBatch();
        hiveRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        user = userRepository.save(
                User.builder()
                        .username("hive-user")
                        .password("password")
                        .name("테스트유저")
                        .businessRegistered(false)
                        .build()
        );
        hive = hiveRepository.save(
                Hive.builder()
                        .macAddress("AA:BB:CC:DD:EE:FF")
                        .name("테스트 벌통")
                        .region("서울")
                        .location("강남구")
                        .user(user)
                        .build()
        );
    }

    @Nested
    @DisplayName("교체 기록 등록")
    class CreateReplacementHistory {

        @Test
        @DisplayName("최초 교체 기록을 등록한다.")
        void createFirstHistory() {
            //given
            LocalDate replacedAt = LocalDate.of(2025, 1, 1);
            HiveReplacementHistoryCreateRequest request = new HiveReplacementHistoryCreateRequest(replacedAt);

            //when
            HiveReplacementHistoryCreateResponse response =
                    hiveReplacementHistoryService.createReplacementHistory(hive.getId(), user.getId(), request);

            //then
            assertThat(response.replacementHistoryId()).isNotNull();

            HiveReplacementHistory saved = hiveReplacementHistoryRepository.findById(response.replacementHistoryId()).orElseThrow();
            assertThat(saved.getReplacedAt()).isEqualTo(replacedAt);
            assertThat(saved.getUsageDays()).isNull();
        }

        @Test
        @DisplayName("두 번째 교체 기록 등록 시 이전 기록의 사용 일수가 업데이트된다.")
        void createSecondHistoryUpdatesPreviousUsageDays() {
            //given
            LocalDate firstDate = LocalDate.of(2025, 1, 1);
            LocalDate secondDate = LocalDate.of(2025, 2, 1);

            HiveReplacementHistory firstHistory = createHistory(hive, firstDate);
            HiveReplacementHistoryCreateRequest request = new HiveReplacementHistoryCreateRequest(secondDate);

            //when
            hiveReplacementHistoryService.createReplacementHistory(hive.getId(), user.getId(), request);

            //then
            HiveReplacementHistory updatedFirst = hiveReplacementHistoryRepository.findById(firstHistory.getId()).orElseThrow();
            long expectedUsageDays = ChronoUnit.DAYS.between(firstDate, secondDate);
            assertThat(updatedFirst.getUsageDays()).isEqualTo(expectedUsageDays);
        }

        @Test
        @DisplayName("존재하지 않는 벌통에 교체 기록을 등록하려는 경우 예외가 발생한다.")
        void createHistoryWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;
            HiveReplacementHistoryCreateRequest request = new HiveReplacementHistoryCreateRequest(LocalDate.of(2025, 1, 1));

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.createReplacementHistory(notFoundHiveId, user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("최신 교체 기록의 날짜와 동일하거나 이전 날짜로 등록하려는 경우 예외가 발생한다.")
        void createHistoryWithInvalidDate() {
            //given
            createHistory(hive, LocalDate.of(2025, 3, 1));
            HiveReplacementHistoryCreateRequest request = new HiveReplacementHistoryCreateRequest(LocalDate.of(2025, 2, 1));

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.createReplacementHistory(hive.getId(), user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_REPLACEMENT_HISTORY_INVALID_DATE);
        }
    }

    @Nested
    @DisplayName("교체 기록 상세 조회")
    class GetReplacementHistoryDetail {

        @Test
        @DisplayName("사용 일수가 설정된 교체 기록 상세를 조회한다.")
        void getDetailWithUsageDays() {
            //given
            LocalDate firstDate = LocalDate.of(2025, 1, 1);
            LocalDate secondDate = LocalDate.of(2025, 2, 10);
            HiveReplacementHistory firstHistory = createHistory(hive, firstDate);
            firstHistory.updateUsageDays(ChronoUnit.DAYS.between(firstDate, secondDate));
            hiveReplacementHistoryRepository.save(firstHistory);

            //when
            HiveReplacementHistoryDetailResponse response =
                    hiveReplacementHistoryService.getReplacementHistoryDetail(hive.getId(), firstHistory.getId(), user.getId());

            //then
            assertThat(response.replacementHistoryId()).isEqualTo(firstHistory.getId());
            assertThat(response.replacedAt()).isEqualTo(firstDate);
            assertThat(response.usageDays()).isEqualTo(ChronoUnit.DAYS.between(firstDate, secondDate));
        }

        @Test
        @DisplayName("사용 일수가 null인 최신 교체 기록 조회 시 오늘까지의 일수를 반환한다.")
        void getDetailWithNullUsageDays() {
            //given
            LocalDate replacedAt = LocalDate.of(2025, 6, 1);
            HiveReplacementHistory history = createHistory(hive, replacedAt);

            //when
            HiveReplacementHistoryDetailResponse response =
                    hiveReplacementHistoryService.getReplacementHistoryDetail(hive.getId(), history.getId(), user.getId());

            //then
            long expectedUsageDays = ChronoUnit.DAYS.between(replacedAt, LocalDate.now());
            assertThat(response.usageDays()).isEqualTo(expectedUsageDays);
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 교체 기록을 조회하려는 경우 예외가 발생한다.")
        void getDetailWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;
            HiveReplacementHistory history = createHistory(hive, LocalDate.of(2025, 1, 1));

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.getReplacementHistoryDetail(notFoundHiveId, history.getId(), user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 교체 기록을 조회하려는 경우 예외가 발생한다.")
        void getDetailWithNotFoundHistory() {
            //given
            Long notFoundHistoryId = -1L;

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.getReplacementHistoryDetail(hive.getId(), notFoundHistoryId, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("교체 기록 목록 조회")
    class GetAllReplacementHistories {

        @Test
        @DisplayName("벌통의 교체 기록 목록을 조회한다.")
        void getAllHistories() {
            //given
            createHistory(hive, LocalDate.of(2025, 1, 1));
            createHistory(hive, LocalDate.of(2025, 2, 1));
            createHistory(hive, LocalDate.of(2025, 3, 1));

            //when
            Slice<?> result = hiveReplacementHistoryService.getAllReplacementHistories(
                    hive.getId(), user.getId(), PageRequest.of(0, 10));

            //then
            assertThat(result.getContent()).hasSize(3);
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 교체 기록 목록을 조회하려는 경우 예외가 발생한다.")
        void getAllHistoriesWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.getAllReplacementHistories(
                            notFoundHiveId, user.getId(), PageRequest.of(0, 10)))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("교체 기록 수정")
    class UpdateReplacementHistory {

        @Test
        @DisplayName("단일 교체 기록의 날짜를 수정한다.")
        void updateSingleHistory() {
            //given
            HiveReplacementHistory history = createHistory(hive, LocalDate.of(2025, 1, 1));
            LocalDate newDate = LocalDate.of(2025, 1, 15);
            HiveReplacementHistoryUpdateRequest request = new HiveReplacementHistoryUpdateRequest(newDate);

            //when
            hiveReplacementHistoryService.updateReplacementHistory(hive.getId(), history.getId(), user.getId(), request);

            //then
            HiveReplacementHistory updated = hiveReplacementHistoryRepository.findById(history.getId()).orElseThrow();
            assertThat(updated.getReplacedAt()).isEqualTo(newDate);
        }

        @Test
        @DisplayName("이전/이후 기록이 있을 때 수정하면 이전 기록과 현재 기록의 사용 일수가 재계산된다.")
        void updateMiddleHistoryRecalculatesUsageDays() {
            //given
            LocalDate olderDate = LocalDate.of(2025, 1, 1);
            LocalDate targetDate = LocalDate.of(2025, 2, 1);
            LocalDate newerDate = LocalDate.of(2025, 3, 1);

            HiveReplacementHistory olderHistory = createHistory(hive, olderDate);
            HiveReplacementHistory targetHistory = createHistory(hive, targetDate);
            createHistory(hive, newerDate);

            LocalDate updatedDate = LocalDate.of(2025, 2, 15);
            HiveReplacementHistoryUpdateRequest request = new HiveReplacementHistoryUpdateRequest(updatedDate);

            //when
            hiveReplacementHistoryService.updateReplacementHistory(hive.getId(), targetHistory.getId(), user.getId(), request);

            //then
            HiveReplacementHistory updatedOlder = hiveReplacementHistoryRepository.findById(olderHistory.getId()).orElseThrow();
            HiveReplacementHistory updatedTarget = hiveReplacementHistoryRepository.findById(targetHistory.getId()).orElseThrow();

            assertThat(updatedOlder.getUsageDays()).isEqualTo(ChronoUnit.DAYS.between(olderDate, updatedDate));
            assertThat(updatedTarget.getUsageDays()).isEqualTo(ChronoUnit.DAYS.between(updatedDate, newerDate));
            assertThat(updatedTarget.getReplacedAt()).isEqualTo(updatedDate);
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 교체 기록을 수정하려는 경우 예외가 발생한다.")
        void updateWithNotFoundHive() {
            //given
            HiveReplacementHistory history = createHistory(hive, LocalDate.of(2025, 1, 1));
            Long notFoundHiveId = -1L;
            HiveReplacementHistoryUpdateRequest request = new HiveReplacementHistoryUpdateRequest(LocalDate.of(2025, 1, 15));

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.updateReplacementHistory(notFoundHiveId, history.getId(), user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 교체 기록을 수정하려는 경우 예외가 발생한다.")
        void updateNotFoundHistory() {
            //given
            Long notFoundHistoryId = -1L;
            HiveReplacementHistoryUpdateRequest request = new HiveReplacementHistoryUpdateRequest(LocalDate.of(2025, 1, 15));

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.updateReplacementHistory(hive.getId(), notFoundHistoryId, user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND);
        }

        @Test
        @DisplayName("이전 기록의 날짜와 동일하거나 이전 날짜로 수정하려는 경우 예외가 발생한다.")
        void updateWithDateBeforeOrEqualOlderHistory() {
            //given
            createHistory(hive, LocalDate.of(2025, 1, 1));
            HiveReplacementHistory targetHistory = createHistory(hive, LocalDate.of(2025, 3, 1));
            HiveReplacementHistoryUpdateRequest request = new HiveReplacementHistoryUpdateRequest(LocalDate.of(2025, 1, 1));

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.updateReplacementHistory(hive.getId(), targetHistory.getId(), user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_REPLACEMENT_HISTORY_INVALID_DATE);
        }

        @Test
        @DisplayName("이후 기록의 날짜와 동일하거나 이후 날짜로 수정하려는 경우 예외가 발생한다.")
        void updateWithDateAfterOrEqualNewerHistory() {
            //given
            HiveReplacementHistory targetHistory = createHistory(hive, LocalDate.of(2025, 1, 1));
            createHistory(hive, LocalDate.of(2025, 3, 1));
            HiveReplacementHistoryUpdateRequest request = new HiveReplacementHistoryUpdateRequest(LocalDate.of(2025, 3, 1));

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.updateReplacementHistory(hive.getId(), targetHistory.getId(), user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_REPLACEMENT_HISTORY_INVALID_DATE);
        }
    }

    @Nested
    @DisplayName("교체 기록 삭제")
    class DeleteReplacementHistory {

        @Test
        @DisplayName("최신 교체 기록을 삭제하면 이전 기록의 사용 일수가 null로 초기화된다.")
        void deleteLatestHistoryClearsOlderUsageDays() {
            //given
            LocalDate olderDate = LocalDate.of(2025, 1, 1);
            LocalDate latestDate = LocalDate.of(2025, 2, 1);

            HiveReplacementHistory olderHistory = createHistory(hive, olderDate);
            olderHistory.updateUsageDays(ChronoUnit.DAYS.between(olderDate, latestDate));
            hiveReplacementHistoryRepository.save(olderHistory);

            HiveReplacementHistory latestHistory = createHistory(hive, latestDate);

            //when
            hiveReplacementHistoryService.deleteReplacementHistory(hive.getId(), latestHistory.getId(), user.getId());

            //then
            assertThat(hiveReplacementHistoryRepository.existsById(latestHistory.getId())).isFalse();
            HiveReplacementHistory updatedOlder = hiveReplacementHistoryRepository.findById(olderHistory.getId()).orElseThrow();
            assertThat(updatedOlder.getUsageDays()).isNull();
        }

        @Test
        @DisplayName("중간 교체 기록을 삭제하면 이전 기록의 사용 일수가 재계산된다.")
        void deleteMiddleHistoryRecalculatesOlderUsageDays() {
            //given
            LocalDate olderDate = LocalDate.of(2025, 1, 1);
            LocalDate middleDate = LocalDate.of(2025, 2, 1);
            LocalDate newerDate = LocalDate.of(2025, 3, 1);

            HiveReplacementHistory olderHistory = createHistory(hive, olderDate);
            HiveReplacementHistory middleHistory = createHistory(hive, middleDate);
            createHistory(hive, newerDate);

            //when
            hiveReplacementHistoryService.deleteReplacementHistory(hive.getId(), middleHistory.getId(), user.getId());

            //then
            assertThat(hiveReplacementHistoryRepository.existsById(middleHistory.getId())).isFalse();
            HiveReplacementHistory updatedOlder = hiveReplacementHistoryRepository.findById(olderHistory.getId()).orElseThrow();
            long expectedUsageDays = ChronoUnit.DAYS.between(olderDate, newerDate);
            assertThat(updatedOlder.getUsageDays()).isEqualTo(expectedUsageDays);
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 교체 기록을 삭제하려는 경우 예외가 발생한다.")
        void deleteWithNotFoundHive() {
            //given
            HiveReplacementHistory history = createHistory(hive, LocalDate.of(2025, 1, 1));
            Long notFoundHiveId = -1L;

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.deleteReplacementHistory(notFoundHiveId, history.getId(), user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 교체 기록을 삭제하려는 경우 예외가 발생한다.")
        void deleteNotFoundHistory() {
            //given
            Long notFoundHistoryId = -1L;

            //when - then
            assertThatThrownBy(() ->
                    hiveReplacementHistoryService.deleteReplacementHistory(hive.getId(), notFoundHistoryId, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_REPLACEMENT_HISTORY_NOT_FOUND);
        }
    }

    private HiveReplacementHistory createHistory(Hive targetHive, LocalDate replacedAt) {
        return hiveReplacementHistoryRepository.save(
                HiveReplacementHistory.builder()
                        .hive(targetHive)
                        .replacedAt(replacedAt)
                        .build()
        );
    }
}
