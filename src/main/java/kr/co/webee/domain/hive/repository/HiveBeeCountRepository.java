package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveBeeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiveBeeCountRepository extends JpaRepository<HiveBeeCount, Long> {
}
