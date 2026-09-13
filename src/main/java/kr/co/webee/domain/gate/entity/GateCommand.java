package kr.co.webee.domain.gate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.gate.type.GateCardType;
import kr.co.webee.domain.gate.type.GateCommandOperation;
import kr.co.webee.domain.gate.type.GateCommandStatus;
import kr.co.webee.domain.gate.type.GateExecutionStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GateCommand extends BaseTimeEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_id", nullable = false)
    private Gate gate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateCommandOperation operation;

    // CANCEL 요청 시 취소 대상 commandId
    private String targetCommandId;

    @Enumerated(EnumType.STRING)
    private GateCardType cardType;

    @Column(length = 40)
    private String title;

    @Column(length = 40)
    private String memo;

    @Column(columnDefinition = "TEXT")
    private String payloadJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GateCommandStatus status;

    private String detail;

    @Enumerated(EnumType.STRING)
    private GateExecutionStatus executionStatus;

    private LocalDateTime appliedAt;

    @Builder
    private GateCommand(String id, Gate gate, GateCommandOperation operation, String targetCommandId,
                        GateCardType cardType, String title, String memo, String payloadJson) {
        this.id = Objects.requireNonNull(id);
        this.gate = Objects.requireNonNull(gate);
        this.operation = operation != null ? operation : GateCommandOperation.EXECUTE;
        this.targetCommandId = targetCommandId;
        this.cardType = cardType;
        this.title = title;
        this.memo = memo;
        this.payloadJson = payloadJson;
        this.status = GateCommandStatus.PENDING;
    }

    public void complete(String resultStatus, String detail) {
        this.status = "OK".equals(resultStatus) ? GateCommandStatus.SUCCESS : GateCommandStatus.FAILED;
        this.detail = detail;
    }

    public void timeout() {
        this.status = GateCommandStatus.TIMEOUT;
    }

    public void activate() {
        this.executionStatus = GateExecutionStatus.ACTIVE;
        this.appliedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.executionStatus = GateExecutionStatus.CANCELLED;
    }
}
