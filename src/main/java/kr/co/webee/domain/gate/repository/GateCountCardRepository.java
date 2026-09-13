package kr.co.webee.domain.gate.repository;

import kr.co.webee.domain.gate.entity.GateCountCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GateCountCardRepository extends JpaRepository<GateCountCard, Long> {
    List<GateCountCard> findAllByUserId(Long userId);
    Optional<GateCountCard> findByIdAndUserId(Long id, Long userId);
}
