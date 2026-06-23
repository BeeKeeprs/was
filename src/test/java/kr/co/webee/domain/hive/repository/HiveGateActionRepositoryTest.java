package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.annotation.RepositoryTest;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveGateAction;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class HiveGateActionRepositoryTest {

    @Autowired
    private HiveGateActionRepository hiveGateActionRepository;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    private Hive hive;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(TestFixture.createUser("gate-repo-user"));
        hive = hiveRepository.save(TestFixture.createHive(null, user));
    }

    @Nested
    @DisplayName("hiveId로 개폐기 동작 목록 조회")
    class FindAllByHiveId {

        @Test
        @DisplayName("벌통 ID에 해당하는 개폐기 동작 목록을 조회한다.")
        void findAllByHiveId() {
            //given
            hiveGateActionRepository.save(TestFixture.createHiveGateAction("아침 열기", GateActionType.OPEN_ONLY, LocalTime.of(8, 0), hive));
            hiveGateActionRepository.save(TestFixture.createHiveGateAction("저녁 닫기", GateActionType.CLOSE_ONLY, LocalTime.of(20, 0), hive));

            //when
            List<HiveGateAction> result = hiveGateActionRepository.findAllByHiveId(hive.getId());

            //then
            assertThat(result).hasSize(2)
                    .extracting("title")
                    .containsExactlyInAnyOrder("아침 열기", "저녁 닫기");
        }

        @Test
        @DisplayName("등록된 개폐기 동작이 없는 경우 빈 목록을 반환한다.")
        void findAllByHiveIdEmpty() {
            //when
            List<HiveGateAction> result = hiveGateActionRepository.findAllByHiveId(hive.getId());

            //then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("id와 hiveId로 개폐기 동작 단건 조회")
    class FindByIdAndHiveId {

        @Test
        @DisplayName("id와 hiveId가 일치하는 개폐기 동작을 조회한다.")
        void findByIdAndHiveId() {
            //given
            HiveGateAction saved = hiveGateActionRepository.save(
                    TestFixture.createHiveGateAction("새벽 환기", GateActionType.OPEN_CLOSE, LocalTime.of(6, 0), hive));

            //when
            Optional<HiveGateAction> result = hiveGateActionRepository.findByIdAndHiveId(saved.getId(), hive.getId());

            //then
            assertThat(result).isPresent();
            assertThat(result.get().getTitle()).isEqualTo("새벽 환기");
        }

        @Test
        @DisplayName("존재하지 않는 id로 조회하면 빈 Optional을 반환한다.")
        void findByIdAndHiveIdNotFound() {
            //when
            Optional<HiveGateAction> result = hiveGateActionRepository.findByIdAndHiveId(-1L, hive.getId());

            //then
            assertThat(result).isEmpty();
        }
    }
}
