package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.Hive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiveRepository extends JpaRepository<Hive, Long> {

    boolean existsBySerialNumber(String serialNumber);

    List<Hive> findByUserId(Long userId);
}
