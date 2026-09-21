package kr.co.webee.domain.gate.repository;

import kr.co.webee.domain.gate.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {
    boolean existsByMacAddress(String macAddress);

    Optional<Gate> findByMacAddress(String macAddress);

    Optional<Gate> findByIdAndUserId(Long id, Long userId);

    List<Gate> findAllByUserId(Long userId);
}
