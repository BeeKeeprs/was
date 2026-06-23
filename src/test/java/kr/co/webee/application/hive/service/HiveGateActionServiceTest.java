package kr.co.webee.application.hive.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
import kr.co.webee.application.hive.dto.request.HiveGateActionUpdateRequest;
import kr.co.webee.application.hive.dto.response.HiveGateActionDetailResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionListResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionRegisterResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveGateAction;
import kr.co.webee.domain.hive.repository.HiveGateActionRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.type.GateActionType;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class HiveGateActionServiceTest {

    @Autowired
    private HiveGateActionService hiveGateActionService;

    @Autowired
    private HiveGateActionRepository hiveGateActionRepository;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Hive hive;

    @BeforeEach
    void setUp() {
        hiveGateActionRepository.deleteAll();
        hiveRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(TestFixture.createUser("gate-user"));
        hive = hiveRepository.save(TestFixture.createHive(null, user));
    }

    @Nested
    @DisplayName("개폐기 동작 등록")
    class RegisterHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 등록한다.")
        void registerHiveGateAction() {
            //given
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "새벽 환기", GateActionType.OPEN_ONLY, LocalTime.of(9, 0), false
            );

            //when
            HiveGateActionRegisterResponse response =
                    hiveGateActionService.registerHiveGateAction(hive.getId(), user.getId(), request);

            //then
            assertThat(response.id()).isNotNull();

            HiveGateAction saved = hiveGateActionRepository.findById(response.id()).orElseThrow();
            assertThat(saved.getTitle()).isEqualTo("새벽 환기");
            assertThat(saved.getActionType()).isEqualTo(GateActionType.OPEN_ONLY);
            assertThat(saved.getActionTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(saved.isRepeatEnabled()).isFalse();
        }

        @Test
        @DisplayName("반복 활성화 상태로 개폐기 동작을 등록한다.")
        void registerHiveGateActionWithRepeat() {
            //given
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "매일 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), true
            );

            //when
            HiveGateActionRegisterResponse response =
                    hiveGateActionService.registerHiveGateAction(hive.getId(), user.getId(), request);

            //then
            HiveGateAction saved = hiveGateActionRepository.findById(response.id()).orElseThrow();
            assertThat(saved.isRepeatEnabled()).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 벌통에 개폐기 동작을 등록하려는 경우 예외가 발생한다.")
        void registerWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "새벽 환기", GateActionType.OPEN_ONLY, LocalTime.of(9, 0), false
            );

            //when - then
            assertThatThrownBy(() ->
                    hiveGateActionService.registerHiveGateAction(notFoundHiveId, user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 사용자의 벌통에 개폐기 동작을 등록하려는 경우 예외가 발생한다.")
        void registerWithOtherUserHive() {
            //given
            User otherUser = userRepository.save(TestFixture.createUser("other-user"));
            HiveGateActionRegisterRequest request = new HiveGateActionRegisterRequest(
                    "새벽 환기", GateActionType.OPEN_ONLY, LocalTime.of(9, 0), false
            );

            //when - then
            assertThatThrownBy(() ->
                    hiveGateActionService.registerHiveGateAction(hive.getId(), otherUser.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("하나의 벌통에 여러 개폐기 동작을 등록할 수 있다.")
        void registerMultipleGateActions() {
            //given
            hiveGateActionService.registerHiveGateAction(hive.getId(), user.getId(),
                    new HiveGateActionRegisterRequest("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), true));
            hiveGateActionService.registerHiveGateAction(hive.getId(), user.getId(),
                    new HiveGateActionRegisterRequest("저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), true));

            //when
            List<HiveGateAction> actions = hiveGateActionRepository.findAllByHiveId(hive.getId());

            //then
            assertThat(actions).hasSize(2);
        }
    }

    @Nested
    @DisplayName("개폐기 동작 목록 조회")
    class GetAllHiveGateActionList {

        @Test
        @DisplayName("벌통의 개폐기 동작 목록을 조회한다.")
        void getAllHiveGateActionList() {
            //given
            hiveGateActionRepository.save(TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));
            hiveGateActionRepository.save(TestFixture.createHiveGateAction("저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), hive));

            //when
            List<HiveGateActionListResponse> result = hiveGateActionService.getAllHiveGateActionList(hive.getId(), user.getId());

            //then
            assertThat(result).hasSize(2)
                    .extracting("title")
                    .containsExactlyInAnyOrder("아침 열기", "저녁 닫기");
        }

        @Test
        @DisplayName("등록된 개폐기 동작이 없으면 빈 목록을 반환한다.")
        void getAllHiveGateActionListEmpty() {
            //when
            List<HiveGateActionListResponse> result = hiveGateActionService.getAllHiveGateActionList(hive.getId(), user.getId());

            //then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 개폐기 동작 목록을 조회하려는 경우 예외가 발생한다.")
        void getAllHiveGateActionListWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.getAllHiveGateActionList(notFoundHiveId, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("개폐기 동작 단건 조회")
    class GetHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 단건 조회한다.")
        void getHiveGateAction() {
            //given
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));

            //when
            HiveGateActionDetailResponse result = hiveGateActionService.getHiveGateAction(hive.getId(), user.getId(), saved.getId());

            //then
            assertThat(result.id()).isEqualTo(saved.getId());
            assertThat(result.title()).isEqualTo("아침 열기");
            assertThat(result.actionType()).isEqualTo(GateActionType.OPEN_ONLY);
            assertThat(result.actionTime()).isEqualTo(LocalTime.of(8, 0));
        }

        @Test
        @DisplayName("존재하지 않는 개폐기 동작을 조회하려는 경우 예외가 발생한다.")
        void getHiveGateActionNotFound() {
            //given
            Long notFoundActionId = -1L;

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.getHiveGateAction(hive.getId(), user.getId(), notFoundActionId))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_GATE_ACTION_NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 개폐기 동작을 조회하려는 경우 예외가 발생한다.")
        void getHiveGateActionWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.getHiveGateAction(notFoundHiveId, user.getId(), saved.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 벌통의 개폐기 동작을 조회하려는 경우 예외가 발생한다.")
        void getHiveGateActionFromOtherHive() {
            //given
            Hive otherHive = hiveRepository.save(TestFixture.createHive(null, user));
            HiveGateAction action = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), otherHive));

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.getHiveGateAction(hive.getId(), user.getId(), action.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_GATE_ACTION_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("개폐기 동작 수정")
    class UpdateHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 수정한다.")
        void updateHiveGateAction() {
            //given
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));
            HiveGateActionUpdateRequest request = new HiveGateActionUpdateRequest(
                    "저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), true
            );

            //when
            HiveGateActionDetailResponse result = hiveGateActionService.updateHiveGateAction(hive.getId(), user.getId(), saved.getId(), request);

            //then
            assertThat(result.title()).isEqualTo("저녁 닫기");
            assertThat(result.actionType()).isEqualTo(GateActionType.CLOSE_ONLY);
            assertThat(result.actionTime()).isEqualTo(LocalTime.of(20, 0));
            assertThat(result.repeatEnabled()).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 개폐기 동작을 수정하려는 경우 예외가 발생한다.")
        void updateHiveGateActionNotFound() {
            //given
            Long notFoundActionId = -1L;
            HiveGateActionUpdateRequest request = new HiveGateActionUpdateRequest(
                    "저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), false
            );

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.updateHiveGateAction(hive.getId(), user.getId(), notFoundActionId, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_GATE_ACTION_NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 개폐기 동작을 수정하려는 경우 예외가 발생한다.")
        void updateHiveGateActionWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));
            HiveGateActionUpdateRequest request = new HiveGateActionUpdateRequest(
                    "저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), false
            );

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.updateHiveGateAction(notFoundHiveId, user.getId(), saved.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 사용자의 벌통에 있는 개폐기 동작을 수정하려는 경우 예외가 발생한다.")
        void updateHiveGateActionWithOtherUserHive() {
            //given
            User otherUser = userRepository.save(TestFixture.createUser("other-user"));
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));
            HiveGateActionUpdateRequest request = new HiveGateActionUpdateRequest(
                    "저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), false
            );

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.updateHiveGateAction(hive.getId(), otherUser.getId(), saved.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("개폐기 동작 삭제")
    class DeleteHiveGateAction {

        @Test
        @DisplayName("개폐기 동작을 삭제한다.")
        void deleteHiveGateAction() {
            //given
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));

            //when
            hiveGateActionService.deleteHiveGateAction(hive.getId(), user.getId(), saved.getId());

            //then
            assertThat(hiveGateActionRepository.findById(saved.getId())).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 개폐기 동작을 삭제하려는 경우 예외가 발생한다.")
        void deleteHiveGateActionNotFound() {
            //given
            Long notFoundActionId = -1L;

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.deleteHiveGateAction(hive.getId(), user.getId(), notFoundActionId))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_GATE_ACTION_NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 개폐기 동작을 삭제하려는 경우 예외가 발생한다.")
        void deleteHiveGateActionWithNotFoundHive() {
            //given
            Long notFoundHiveId = -1L;
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.deleteHiveGateAction(notFoundHiveId, user.getId(), saved.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 사용자의 벌통에 있는 개폐기 동작을 삭제하려는 경우 예외가 발생한다.")
        void deleteHiveGateActionWithOtherUserHive() {
            //given
            User otherUser = userRepository.save(TestFixture.createUser("other-user"));
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));

            //when - then
            assertThatThrownBy(() -> hiveGateActionService.deleteHiveGateAction(hive.getId(), otherUser.getId(), saved.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }
}
