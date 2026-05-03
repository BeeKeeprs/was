package kr.co.webee.domain.hive.repository;

import kr.co.webee.domain.hive.entity.Hive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HiveRepository extends JpaRepository<Hive, Long> {

    boolean existsBySerialNumber(String serialNumber);

    List<Hive> findByUserId(Long userId);

    Optional<Hive> findByIdAndUserId(Long id, Long userId);
}
