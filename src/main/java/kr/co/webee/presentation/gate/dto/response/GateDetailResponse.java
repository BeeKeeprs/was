package kr.co.webee.presentation.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.gate.entity.Gate;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "개폐기 상세 response")
public record GateDetailResponse(
        @Schema(description = "개폐기 ID", example = "1")
        Long gateId,

        @Schema(description = "개폐기 이름", example = "딸기 온실 개폐기")
        String name,

        @Schema(description = "지역", example = "충청북도 청주시 오창읍")
        String region,

        @Schema(description = "설치 위치", example = "온실 남쪽 출입구")
        String location,

        @Schema(description = "MAC 주소", example = "AA:BB:CC:DD:EE:FF")
        String macAddress,

        @Schema(description = "메모", example = "점검 필요", nullable = true)
        String memo,

        @Schema(description = "연결 상태")
        boolean isConnected,

        @Schema(description = "마지막 연결 시각", nullable = true)
        LocalDateTime lastConnectedAt,

        @Schema(description = "등록 일시", example = "2024-03-15T09:00:00")
        LocalDateTime createdAt,

        @Schema(description = "수정 일시", example = "2024-03-16T10:00:00", nullable = true)
        LocalDateTime modifiedAt
) {
    public static GateDetailResponse from(Gate gate) {
        return GateDetailResponse.builder()
                .gateId(gate.getId())
                .name(gate.getName())
                .region(gate.getRegion())
                .location(gate.getLocation())
                .macAddress(gate.getMacAddress())
                .memo(gate.getMemo())
                .isConnected(gate.isConnected())
                .lastConnectedAt(gate.getLastConnectedAt())
                .createdAt(gate.getCreatedAt())
                .modifiedAt(gate.getModifiedAt())
                .build();
    }
}
