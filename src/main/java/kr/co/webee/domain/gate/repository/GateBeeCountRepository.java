package kr.co.webee.domain.gate.repository;

import kr.co.webee.domain.gate.entity.GateBeeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GateBeeCountRepository extends JpaRepository<GateBeeCount, Long> {
    List<GateBeeCount> findByGateIdAndRecordedAtBetween(Long gateId, LocalDateTime start, LocalDateTime end);

    void deleteAllByGateId(Long gateId);
}
