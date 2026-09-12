package kr.co.webee.domain.gate.repository;

import kr.co.webee.domain.gate.entity.GateTimeCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GateTimeCardRepository extends JpaRepository<GateTimeCard, Long> {
    List<GateTimeCard> findAllByGateId(Long gateId);
    Optional<GateTimeCard> findByIdAndGateId(Long id, Long gateId);
}
