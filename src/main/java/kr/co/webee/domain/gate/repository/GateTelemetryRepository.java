package kr.co.webee.domain.gate.repository;

import kr.co.webee.domain.gate.entity.GateTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GateTelemetryRepository extends JpaRepository<GateTelemetry, Long> {
}
