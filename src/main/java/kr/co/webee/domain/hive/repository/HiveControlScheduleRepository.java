package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HiveControlScheduleRepository extends JpaRepository<HiveControlSchedule, Long> {

    List<HiveControlSchedule> findAllByHiveId(Long hiveId);

    List<HiveControlSchedule> findAllByEnabled(boolean enabled);

    Optional<HiveControlSchedule> findByIdAndHiveId(Long id, Long hiveId);

    @Query("""
            SELECT COUNT(s) > 0
            FROM HiveControlSchedule s
            WHERE s.hive.id = :hiveId
              AND s.enabled = true
              AND s.startTime <= :now
              AND s.endTime > :now
            """)
    boolean existsActiveSchedule(@Param("hiveId") Long hiveId, @Param("now") LocalTime now);

    @Modifying
    @Query("DELETE FROM HiveControlSchedule s WHERE s.hive.id = :hiveId")
    void deleteAllByHiveId(@Param("hiveId") Long hiveId);
}
