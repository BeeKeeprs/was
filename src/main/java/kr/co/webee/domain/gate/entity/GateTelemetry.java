package kr.co.webee.domain.gate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GateTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Gate gate;

    private Double temperature;

    private Double humidity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @Builder
    private GateTelemetry(Gate gate, Double temperature, Double humidity, LocalDateTime recordedAt) {
        this.gate = Objects.requireNonNull(gate, "gate는 null이 될 수 없습니다.");
        this.temperature = temperature;
        this.humidity = humidity;
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt은 null이 될 수 없습니다.");
    }
}