package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.type.ControlType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HiveControlRepository extends JpaRepository<HiveControl, Long> {
    Optional<HiveControl> findByHiveIdAndType(Long hiveId, ControlType type);

    List<HiveControl> findAllByHiveId(Long hiveId);

    @Modifying
    @Query("DELETE FROM HiveControl c WHERE c.hive.id = :hiveId")
    void deleteAllByHiveId(@Param("hiveId") Long hiveId);
}
