package kr.co.webee.application.gate.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.gate.entity.GateCommand;
import kr.co.webee.domain.gate.type.GateCardType;
import kr.co.webee.domain.gate.type.GateExecutionStatus;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Schema(description = "개폐기 현재 적용 중인 명령 조회 응답")
public record GateCurrentCommandResponse(
        @Schema(description = "명령 ID")
        String commandId,

        @Schema(description = "카드 종류")
        GateCardType cardType,

        @Schema(description = "카드 제목")
        String title,

        @Schema(description = "메모")
        String memo,

        @Schema(description = "cardType별 payload")
        JsonNode payload,

        @Schema(description = "적용 상태")
        GateExecutionStatus executionStatus,

        @Schema(description = "적용된 시각 (ISO 8601)")
        OffsetDateTime appliedAt
) {
    public static GateCurrentCommandResponse from(GateCommand command, JsonNode parsedPayload) {
        return new GateCurrentCommandResponse(
                command.getId(),
                command.getCardType(),
                command.getTitle(),
                command.getMemo(),
                parsedPayload,
                command.getExecutionStatus(),
                command.getAppliedAt().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime()
        );
    }
}
