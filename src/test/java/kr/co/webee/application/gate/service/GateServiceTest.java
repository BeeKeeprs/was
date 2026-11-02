package kr.co.webee.application.gate.service;

import kr.co.webee.annotation.IntegrationTest;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.repository.GateBeeCountRepository;
import kr.co.webee.domain.gate.repository.GateCommandRepository;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.gate.repository.GateTelemetryRepository;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.gate.dto.request.GateRegisterRequest;
import kr.co.webee.presentation.gate.dto.request.GateUpdateRequest;
import kr.co.webee.presentation.gate.dto.response.GateDetailResponse;
import kr.co.webee.presentation.gate.dto.response.GateListResponse;
import kr.co.webee.presentation.gate.dto.response.GateRegisterResponse;
import kr.co.webee.support.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class GateServiceTest {

    @Autowired
    private GateService gateService;

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private GateTelemetryRepository gateTelemetryRepository;

    @Autowired
    private GateBeeCountRepository gateBeeCountRepository;

    @Autowired
    private GateCommandRepository gateCommandRepository;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User other;
    private Hive hive;

    @BeforeEach
    void setUp() {
        gateTelemetryRepository.deleteAllInBatch();
        gateBeeCountRepository.deleteAllInBatch();
        gateCommandRepository.deleteAllInBatch();
        gateRepository.deleteAllInBatch();
        hiveRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        user = userRepository.save(TestFixture.createUser("gate-user"));
        other = userRepository.save(TestFixture.createUser("other-user"));
        hive = hiveRepository.save(TestFixture.createHive(null, user));
    }

    private GateRegisterRequest registerRequest(String mac, Long hiveId) {
        return GateRegisterRequest.builder()
                .name("개폐기")
                .region("충북")
                .location("남쪽 출입구")
                .macAddress(mac)
                .hiveId(hiveId)
                .build();
    }

    private GateUpdateRequest updateRequest(Long hiveId) {
        return GateUpdateRequest.builder()
                .name("수정된 개폐기")
                .region("충북")
                .location("북쪽 출입구")
                .memo("메모")
                .hiveId(hiveId)
                .build();
    }

    @Nested
    @DisplayName("개폐기 등록")
    class RegisterGate {

        @Test
        @DisplayName("벌통 없이 등록한다.")
        void registerWithoutHive() {
            //when
            GateRegisterResponse response = gateService.registerGate(registerRequest("AA:01", null), user.getId());

            //then
            GateDetailResponse detail = gateService.getGateDetail(response.gateId(), user.getId());
            assertThat(detail.hive()).isNull();
        }

        @Test
        @DisplayName("벌통을 연결해서 등록한다.")
        void registerWithHive() {
            //when
            GateRegisterResponse response = gateService.registerGate(registerRequest("AA:02", hive.getId()), user.getId());

            //then
            GateDetailResponse detail = gateService.getGateDetail(response.gateId(), user.getId());
            assertThat(detail.hive().hiveId()).isEqualTo(hive.getId());
            assertThat(detail.hive().name()).isEqualTo(hive.getName());
        }

        @Test
        @DisplayName("존재하지 않는 벌통이면 예외가 발생하고 개폐기는 저장되지 않는다.")
        void registerWithUnknownHive() {
            //when - then
            assertThatThrownBy(() -> gateService.registerGate(registerRequest("AA:03", 999_999L), user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type").isEqualTo(ErrorType.HIVE_NOT_FOUND);
            assertThat(gateRepository.existsByMacAddress("AA:03")).isFalse();
        }

        @Test
        @DisplayName("다른 사용자의 벌통이면 예외가 발생한다.")
        void registerWithOthersHive() {
            //when - then
            assertThatThrownBy(() -> gateService.registerGate(registerRequest("AA:04", hive.getId()), other.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type").isEqualTo(ErrorType.HIVE_NOT_FOUND);
        }

        @Test
        @DisplayName("이미 등록된 MAC 주소면 예외가 발생한다.")
        void registerDuplicateMacAddress() {
            //given
            gateService.registerGate(registerRequest("AA:05", null), user.getId());

            //when - then
            assertThatThrownBy(() -> gateService.registerGate(registerRequest("AA:05", null), user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type").isEqualTo(ErrorType.GATE_MAC_ADDRESS_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("개폐기 수정")
    class UpdateGate {

        @Test
        @DisplayName("벌통을 연결한다.")
        void linkHive() {
            //given
            Gate gate = gateRepository.save(TestFixture.createGate(null, user, null));

            //when
            gateService.updateGate(gate.getId(), user.getId(), updateRequest(hive.getId()));

            //then
            assertThat(gateService.getGateDetail(gate.getId(), user.getId()).hive().hiveId()).isEqualTo(hive.getId());
        }

        @Test
        @DisplayName("같은 벌통으로 다시 수정해도 정상 처리된다.")
        void linkSameHiveTwice() {
            //given
            Gate gate = gateRepository.save(TestFixture.createGate(null, user, hive));

            //when
            gateService.updateGate(gate.getId(), user.getId(), updateRequest(hive.getId()));

            //then
            assertThat(gateService.getGateDetail(gate.getId(), user.getId()).hive().hiveId()).isEqualTo(hive.getId());
        }

        @Test
        @DisplayName("hiveId가 null이면 연결이 해제된다.")
        void unlinkHive() {
            //given
            Gate gate = gateRepository.save(TestFixture.createGate(null, user, hive));

            //when
            gateService.updateGate(gate.getId(), user.getId(), updateRequest(null));

            //then
            assertThat(gateService.getGateDetail(gate.getId(), user.getId()).hive()).isNull();
        }

        @Test
        @DisplayName("다른 사용자의 벌통으로 수정하면 예외가 발생하고 기존 연결은 유지된다.")
        void updateWithOthersHive() {
            //given
            Hive othersHive = hiveRepository.save(TestFixture.createHive("BB:BB:BB:BB:BB:BB", other));
            Gate gate = gateRepository.save(TestFixture.createGate(null, user, hive));

            //when - then
            assertThatThrownBy(() -> gateService.updateGate(gate.getId(), user.getId(), updateRequest(othersHive.getId())))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type").isEqualTo(ErrorType.HIVE_NOT_FOUND);
            assertThat(gateService.getGateDetail(gate.getId(), user.getId()).hive().hiveId()).isEqualTo(hive.getId());
        }
    }

    @Nested
    @DisplayName("개폐기 삭제")
    class DeleteGate {

        @Test
        @DisplayName("개폐기를 삭제한다.")
        void deleteGate() {
            //given
            Gate gate = gateRepository.save(TestFixture.createGate(null, user, null));

            //when
            gateService.deleteGate(gate.getId(), user.getId());

            //then
            assertThat(gateRepository.findById(gate.getId())).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 개폐기를 삭제하면 예외가 발생한다.")
        void deleteGateNotFound() {
            //when - then
            assertThatThrownBy(() -> gateService.deleteGate(999_999L, user.getId()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("type").isEqualTo(ErrorType.GATE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("개폐기 목록 조회")
    class GetAllGates {

        @Test
        @DisplayName("벌통이 연결된 개폐기와 아닌 개폐기를 함께 조회한다.")
        void getAllGates() {
            //given
            gateRepository.save(TestFixture.createGate("11:01", user, hive));
            gateRepository.save(TestFixture.createGate("11:02", user, null));

            //when
            GateListResponse response = gateService.getAllGates(user.getId());

            //then
            assertThat(response.totalCount()).isEqualTo(2);
            assertThat(response.gates()).filteredOn(g -> g.hive() != null).hasSize(1);
            assertThat(response.gates()).filteredOn(g -> g.hive() == null).hasSize(1);
        }
    }
}
