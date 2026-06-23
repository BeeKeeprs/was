package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HiveTelemetryRepository extends JpaRepository<HiveTelemetry, Long> {

    List<HiveTelemetry> findByHiveIdAndRecordedAtBetween(Long hiveId, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("DELETE FROM HiveTelemetry t WHERE t.hive.id = :hiveId")
    void deleteAllByHiveId(@Param("hiveId") Long hiveId);
}
