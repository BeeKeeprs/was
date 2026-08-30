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
public class GateBeeCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Gate gate;

    @Column(nullable = false)
    private int entranceIn;

    @Column(nullable = false)
    private int entranceOut;

    @Column(nullable = false)
    private int exitIn;

    @Column(nullable = false)
    private int exitOut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @Builder
    private GateBeeCount(Gate gate, int entranceIn, int entranceOut, int exitIn, int exitOut, LocalDateTime recordedAt) {
        this.gate = Objects.requireNonNull(gate, "gate는 null이 될 수 없습니다.");
        this.entranceIn = entranceIn;
        this.entranceOut = entranceOut;
        this.exitIn = exitIn;
        this.exitOut = exitOut;
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt은 null이 될 수 없습니다.");
    }
}