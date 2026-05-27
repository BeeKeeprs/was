package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveBeeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HiveBeeCountRepository extends JpaRepository<HiveBeeCount, Long> {

    List<HiveBeeCount> findByHiveIdAndRecordedAtBetween(Long hiveId, LocalDateTime start, LocalDateTime end);
}
