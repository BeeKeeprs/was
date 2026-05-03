package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.Hive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiveRepository extends JpaRepository<Hive, Long> {
    boolean existsBySerialNumber(String serialNumber);
}
