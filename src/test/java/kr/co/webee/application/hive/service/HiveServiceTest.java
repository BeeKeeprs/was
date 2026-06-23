package kr.co.webee.application.hive.service;

import kr.co.webee.annotation.IntegrationTest;
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
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class HiveServiceTest {

    @Autowired
    private HiveService hiveService;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HiveGateActionRepository hiveGateActionRepository;

    @Autowired
    private HiveBeeCountRepository hiveBeeCountRepository;

    @Autowired
    private HiveControlRepository hiveControlRepository;

    @Autowired
    private HiveControlScheduleRepository hiveControlScheduleRepository;

    @Autowired
    private HiveReplacementHistoryRepository hiveReplacementHistoryRepository;

    @Autowired
    private HiveTelemetryRepository hiveTelemetryRepository;

    private User user;
    private Hive hive;

    @BeforeEach
    void setUp() {
        hiveGateActionRepository.deleteAllInBatch();
        hiveBeeCountRepository.deleteAllInBatch();
        hiveControlRepository.deleteAllInBatch();
        hiveControlScheduleRepository.deleteAllInBatch();
        hiveReplacementHistoryRepository.deleteAllInBatch();
        hiveTelemetryRepository.deleteAllInBatch();
        hiveRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        user = userRepository.save(TestFixture.createUser("hive-user"));
        hive = hiveRepository.save(TestFixture.createHive(null, user));
    }

    @Nested
    @DisplayName("벌통 등록")
    class RegisterHive {

        @Test
        @DisplayName("벌통을 등록한다.")
        void registerHive() {
            //given
            HiveRegisterRequest request = HiveRegisterRequest.builder()
                    .name("새 벌통")
                    .region("경기도")
                    .location("파주시")
                    .macAddress("11:22:33:44:55:66")
                    .build();

            //when
            HiveRegisterResponse response = hiveService.registerHive(request, user.getId());

            //then
            assertThat(response.hiveId()).isNotNull();
        }

        @Test
        @DisplayName("이미 등록된 MAC 주소로 벌통을 등록하려는 경우 예외가 발생한다.")
        void registerHiveWithDuplicateMacAddress() {
            //given
            HiveRegisterRequest request = HiveRegisterRequest.builder()
                    .name("새 벌통")
                    .region("경기도")
                    .location("파주시")
                    .macAddress("AA:BB:CC:DD:EE:FF")
                    .build();

            //when - then
            assertThatThrownBy(() -> hiveService.registerHive(request, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_MAC_ADDRESS_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("벌통 목록 조회")
    class GetAllHives {

        @Test
        @DisplayName("사용자의 벌통 목록을 조회한다.")
        void getAllHives() {
            //when
            HiveListResponse response = hiveService.getAllHives(user.getId());

            //then
            assertThat(response.totalCount()).isEqualTo(1);
            assertThat(response.hives()).hasSize(1);
        }

        @Test
        @DisplayName("등록된 벌통이 없으면 빈 목록을 반환한다.")
        void getAllHivesEmpty() {
            //given
            User newUser = userRepository.save(TestFixture.createUser("no-hive-user"));

            //when
            HiveListResponse response = hiveService.getAllHives(newUser.getId());

            //then
            assertThat(response.totalCount()).isZero();
            assertThat(response.hives()).isEmpty();
        }
    }

    @Nested
    @DisplayName("벌통 상세 조회")
    class GetHiveDetail {

        @Test
        @DisplayName("벌통 상세 정보를 조회한다.")
        void getHiveDetail() {
            //when
            HiveDetailResponse response = hiveService.getHiveDetail(hive.getId(), user.getId());

            //then
            assertThat(response.hiveId()).isEqualTo(hive.getId());
            assertThat(response.name()).isEqualTo("테스트 벌통");
        }

        @Test
        @DisplayName("존재하지 않는 벌통을 조회하려는 경우 예외가 발생한다.")
        void getHiveDetailNotFound() {
            //when - then
            assertThatThrownBy(() -> hiveService.getHiveDetail(999L, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 사용자의 벌통을 조회하려는 경우 예외가 발생한다.")
        void getHiveDetailOfOtherUser() {
            //given
            User otherUser = userRepository.save(TestFixture.createUser("other-user"));

            //when - then
            assertThatThrownBy(() -> hiveService.getHiveDetail(hive.getId(), otherUser.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("벌통 수정")
    class UpdateHive {

        @Test
        @DisplayName("벌통 정보를 수정한다.")
        void updateHive() {
            //given
            HiveUpdateRequest request = HiveUpdateRequest.builder()
                    .name("수정된 벌통")
                    .region("경기도")
                    .location("파주시")
                    .build();

            //when
            hiveService.updateHive(hive.getId(), user.getId(), request);

            //then
            Hive updated = hiveRepository.findById(hive.getId()).orElseThrow();
            assertThat(updated.getName()).isEqualTo("수정된 벌통");
            assertThat(updated.getRegion()).isEqualTo("경기도");
        }

        @Test
        @DisplayName("존재하지 않는 벌통을 수정하려는 경우 예외가 발생한다.")
        void updateHiveNotFound() {
            //given
            HiveUpdateRequest request = HiveUpdateRequest.builder()
                    .name("수정된 벌통")
                    .region("경기도")
                    .location("파주시")
                    .build();

            //when - then
            assertThatThrownBy(() -> hiveService.updateHive(999L, user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("벌통 삭제")
    class DeleteHive {

        @Test
        @DisplayName("벌통을 삭제하면 연관된 자식 엔티티도 모두 삭제된다.")
        void deleteHiveWithChildren() {
            //given
            hiveBeeCountRepository.save(TestFixture.createHiveBeeCount(hive));
            hiveControlRepository.save(TestFixture.createHiveControl(null, hive));
            hiveControlScheduleRepository.save(TestFixture.createHiveControlSchedule(hive));
            hiveReplacementHistoryRepository.save(TestFixture.createHiveReplacementHistory(hive));
            hiveTelemetryRepository.save(TestFixture.createHiveTelemetry(hive));
            hiveGateActionRepository.save(TestFixture.createHiveGateAction(null, null, null, hive));

            //when
            hiveService.deleteHive(hive.getId(), user.getId());

            //then
            assertThat(hiveRepository.findById(hive.getId())).isEmpty();
            assertThat(hiveBeeCountRepository.findAll()).isEmpty();
            assertThat(hiveControlRepository.findAll()).isEmpty();
            assertThat(hiveControlScheduleRepository.findAll()).isEmpty();
            assertThat(hiveReplacementHistoryRepository.findAll()).isEmpty();
            assertThat(hiveTelemetryRepository.findAll()).isEmpty();
            assertThat(hiveGateActionRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 벌통을 삭제하려는 경우 예외가 발생한다.")
        void deleteHiveNotFound() {
            //when - then
            assertThatThrownBy(() -> hiveService.deleteHive(999L, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 사용자의 벌통을 삭제하려는 경우 예외가 발생한다.")
        void deleteHiveOfOtherUser() {
            //given
            User otherUser = userRepository.save(TestFixture.createUser("other-user"));

            //when - then
            assertThatThrownBy(() -> hiveService.deleteHive(hive.getId(), otherUser.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }
}
