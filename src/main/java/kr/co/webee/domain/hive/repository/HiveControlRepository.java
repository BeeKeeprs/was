package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.type.ControlType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HiveControlRepository extends JpaRepository<HiveControl, Long> {
    Optional<HiveControl> findByHiveIdAndType(Long hiveId, ControlType type);
}
