package kr.co.webee.domain.gate.repository;

import kr.co.webee.domain.gate.entity.GateCommand;
import kr.co.webee.domain.gate.type.GateCommandStatus;
import kr.co.webee.domain.gate.type.GateExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GateCommandRepository extends JpaRepository<GateCommand, String> {
    Optional<GateCommand> findByIdAndGateId(String id, Long gateId);
    List<GateCommand> findAllByStatusAndCreatedAtBefore(GateCommandStatus status, LocalDateTime before);
    Optional<GateCommand> findByGateIdAndExecutionStatus(Long gateId, GateExecutionStatus executionStatus);
}
