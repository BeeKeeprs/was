package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveGateAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HiveGateActionRepository extends JpaRepository<HiveGateAction, Long> {

    List<HiveGateAction> findAllByHiveId(Long hiveId);

    Optional<HiveGateAction> findByIdAndHiveId(Long id, Long hiveId);
}
