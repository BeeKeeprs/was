package kr.co.webee.domain.gate.repository;

import jakarta.persistence.EntityManager;
import kr.co.webee.domain.annotation.RepositoryTest;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.support.util.TestFixture;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class GateRepositoryTest {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private HiveRepository hiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("사용자 개폐기 목록 조회 (벌통 fetch join)")
    class FindAllByUserIdWithHive {

        @Test
        @DisplayName("벌통이 있는 개폐기와 없는 개폐기를 모두 조회하고, 벌통은 초기화된 상태다.")
        void findAllWithAndWithoutHive() {
            //given
            User user = userRepository.save(TestFixture.createUser("gate-user"));
            Hive hive = hiveRepository.save(TestFixture.createHive("AA:00:00:00:00:01", user));
            gateRepository.save(TestFixture.createGate("11:00:00:00:00:01", user, hive));
            gateRepository.save(TestFixture.createGate("11:00:00:00:00:02", user, null));
            em.flush();
            em.clear();

            //when
            List<Gate> result = gateRepository.findAllByUserIdWithHive(user.getId());

            //then
            assertThat(result).hasSize(2);
            Gate linked = result.stream().filter(g -> g.getHive() != null).findFirst().orElseThrow();
            assertThat(Hibernate.isInitialized(linked.getHive())).isTrue();
            assertThat(result).filteredOn(g -> g.getHive() == null).hasSize(1);
        }

        @Test
        @DisplayName("다른 사용자의 개폐기는 조회하지 않는다.")
        void findAllExcludesOtherUsers() {
            //given
            User user = userRepository.save(TestFixture.createUser("gate-user"));
            User other = userRepository.save(TestFixture.createUser("other-user"));
            gateRepository.save(TestFixture.createGate("11:00:00:00:00:01", other, null));

            //when
            List<Gate> result = gateRepository.findAllByUserIdWithHive(user.getId());

            //then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("벌통 삭제")
    class DeleteHive {

        @Test
        @DisplayName("연결된 벌통을 삭제하면 개폐기는 남고 hive_id만 NULL이 된다.")
        void deleteHiveSetsNull() {
            //given
            User user = userRepository.save(TestFixture.createUser("gate-user"));
            Hive hive = hiveRepository.save(TestFixture.createHive(null, user));
            Gate gate = gateRepository.save(TestFixture.createGate(null, user, hive));
            em.flush();
            em.clear();

            //when
            em.createNativeQuery("delete from hive where id = :id")
                    .setParameter("id", hive.getId())
                    .executeUpdate();
            em.clear();

            //then
            Gate found = gateRepository.findById(gate.getId()).orElseThrow();
            assertThat(found.getHive()).isNull();
        }
    }
}
