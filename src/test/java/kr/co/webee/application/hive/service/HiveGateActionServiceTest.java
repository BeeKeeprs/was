package kr.co.webee.application.hive.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
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
}
