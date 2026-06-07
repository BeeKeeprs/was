package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiveControlScheduleRepository extends JpaRepository<HiveControlSchedule, Long> {
}
