package kr.co.webee.application.hive.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.application.hive.dto.HivePendingCommand;
import kr.co.webee.application.hive.dto.response.HiveControlCommandProcessResponse;
import kr.co.webee.application.hive.dto.response.HiveControlCommandResponse;
import kr.co.webee.application.hive.dto.response.HiveControlListResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.repository.HiveControlRepository;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.type.ControlType;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.mqtt.config.MqttBrokerConfig;
import kr.co.webee.presentation.hive.dto.request.HiveManualControlRequest;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class HiveControlServiceTest {

    @Autowired
    private HiveControlService hiveControlService;

    @Autowired
    private HiveControlRepository hiveControlRepository;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private MqttBrokerConfig.MqttPublisher mqttPublisher;

    private User user;
    private Hive hive;

    @BeforeEach
    void setUp() {
        hiveControlRepository.deleteAllInBatch();
        hiveRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        user = userRepository.save(TestFixture.createUser("control-user"));
        hive = hiveRepository.save(TestFixture.createHive(null, user));
    }

    @Nested
    @DisplayName("제어 설정 목록 조회")
    class GetControlList {

        @Test
        @DisplayName("등록된 제어 설정 목록을 조회한다.")
        void getControlList() {
            //given
            hiveControlRepository.save(TestFixture.createHiveControl(ControlType.TEMPERATURE, hive));
            hiveControlRepository.save(TestFixture.createHiveControl(ControlType.HUMIDITY, hive));

            //when
            HiveControlListResponse response = hiveControlService.getControlList(hive.getId(), user.getId());

            //then
            assertThat(response.controls()).hasSize(2)
                    .extracting("type")
                    .containsExactlyInAnyOrder(ControlType.TEMPERATURE, ControlType.HUMIDITY);
        }

        @Test
        @DisplayName("등록된 제어 설정이 없으면 목표값이 null인 항목을 반환한다.")
        void getControlListEmpty() {
            //when
            HiveControlListResponse response = hiveControlService.getControlList(hive.getId(), user.getId());

            //then
            assertThat(response.controls()).hasSize(2)
                    .extracting("targetValue")
                    .containsOnlyNulls();
        }

        @Test
        @DisplayName("존재하지 않는 벌통의 제어 설정을 조회하려는 경우 예외가 발생한다.")
        void getControlListHiveNotFound() {
            //when - then
            assertThatThrownBy(() -> hiveControlService.getControlList(999L, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("수동 제어 명령 전송")
    class SetManualControl {

        @Test
        @DisplayName("수동 제어 명령을 전송한다.")
        void setManualControl() {
            //given
            HiveManualControlRequest request = new HiveManualControlRequest(36.5, 60.0);

            //when - then
            hiveControlService.setManualControl(hive.getId(), user.getId(), request);
        }

        @Test
        @DisplayName("존재하지 않는 벌통에 제어 명령을 전송하려는 경우 예외가 발생한다.")
        void setManualControlHiveNotFound() {
            //given
            HiveManualControlRequest request = new HiveManualControlRequest(36.5, 60.0);

            //when - then
            assertThatThrownBy(() -> hiveControlService.setManualControl(999L, user.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 사용자의 벌통에 제어 명령을 전송하려는 경우 예외가 발생한다.")
        void setManualControlOtherUser() {
            //given
            User otherUser = userRepository.save(TestFixture.createUser("other-user"));
            HiveManualControlRequest request = new HiveManualControlRequest(36.5, 60.0);

            //when - then
            assertThatThrownBy(() -> hiveControlService.setManualControl(hive.getId(), otherUser.getId(), request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type")
                    .isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("수동 제어 응답 처리")
    class ProcessManualControlCommandResponse {

        @Test
        @DisplayName("온도 제어 명령 응답을 처리하고 제어 설정을 저장한다.")
        void processTemperatureControlSuccess() {
            //given
            HivePendingCommand pending = HivePendingCommand.of(user.getId(), hive.getId(), 36.5, null);
            HiveControlCommandResponse response = new HiveControlCommandResponse("cmd-001", true, null);

            //when
            HiveControlCommandProcessResponse result = hiveControlService.processManualControlCommandResponse(pending, response);

            //then
            assertThat(result.success()).isTrue();
            assertThat(result.targetTemperature()).isEqualTo(36.5);

            HiveControl saved = hiveControlRepository.findByHiveIdAndType(hive.getId(), ControlType.TEMPERATURE).orElseThrow();
            assertThat(saved.getTargetValue()).isEqualTo(36.5);
        }

        @Test
        @DisplayName("습도 제어 명령 응답을 처리하고 제어 설정을 저장한다.")
        void processHumidityControlSuccess() {
            //given
            HivePendingCommand pending = HivePendingCommand.of(user.getId(), hive.getId(), null, 60.0);
            HiveControlCommandResponse response = new HiveControlCommandResponse("cmd-002", true, null);

            //when
            HiveControlCommandProcessResponse result = hiveControlService.processManualControlCommandResponse(pending, response);

            //then
            assertThat(result.success()).isTrue();
            assertThat(result.targetHumidity()).isEqualTo(60.0);

            HiveControl saved = hiveControlRepository.findByHiveIdAndType(hive.getId(), ControlType.HUMIDITY).orElseThrow();
            assertThat(saved.getTargetValue()).isEqualTo(60.0);
        }

        @Test
        @DisplayName("온습도 제어 명령 응답을 동시에 처리한다.")
        void processBothControlSuccess() {
            //given
            HivePendingCommand pending = HivePendingCommand.of(user.getId(), hive.getId(), 36.5, 60.0);
            HiveControlCommandResponse response = new HiveControlCommandResponse("cmd-003", true, null);

            //when
            HiveControlCommandProcessResponse result = hiveControlService.processManualControlCommandResponse(pending, response);

            //then
            assertThat(result.success()).isTrue();
            assertThat(result.targetTemperature()).isEqualTo(36.5);
            assertThat(result.targetHumidity()).isEqualTo(60.0);
            assertThat(hiveControlRepository.findAllByHiveId(hive.getId())).hasSize(2);
        }

        @Test
        @DisplayName("기존 제어 설정이 있으면 목표값을 업데이트한다.")
        void processControlUpdateExisting() {
            //given
            HiveControl existing = hiveControlRepository.save(HiveControl.create(hive, ControlType.TEMPERATURE));
            existing.updateTargetValue(30.0);
            hiveControlRepository.save(existing);

            HivePendingCommand pending = HivePendingCommand.of(user.getId(), hive.getId(), 36.5, null);
            HiveControlCommandResponse response = new HiveControlCommandResponse("cmd-004", true, null);

            //when
            hiveControlService.processManualControlCommandResponse(pending, response);

            //then
            HiveControl updated = hiveControlRepository.findByHiveIdAndType(hive.getId(), ControlType.TEMPERATURE).orElseThrow();
            assertThat(updated.getTargetValue()).isEqualTo(36.5);
            assertThat(hiveControlRepository.findAllByHiveId(hive.getId())).hasSize(1);
        }

        @Test
        @DisplayName("제어 명령이 실패하면 DB를 변경하지 않고 실패 응답을 반환한다.")
        void processControlFailure() {
            //given
            HivePendingCommand pending = HivePendingCommand.of(user.getId(), hive.getId(), 36.5, 60.0);
            HiveControlCommandResponse response = new HiveControlCommandResponse("cmd-005", false, "기기 오류");

            //when
            HiveControlCommandProcessResponse result = hiveControlService.processManualControlCommandResponse(pending, response);

            //then
            assertThat(result.success()).isFalse();
            assertThat(result.message()).isEqualTo("기기 오류");
            assertThat(hiveControlRepository.findAllByHiveId(hive.getId())).isEmpty();
        }
    }
}
