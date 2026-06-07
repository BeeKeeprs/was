package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiveControlScheduleRepository extends JpaRepository<HiveControlSchedule, Long> {
    List<HiveControlSchedule> findAllByHiveId(Long hiveId);
}
