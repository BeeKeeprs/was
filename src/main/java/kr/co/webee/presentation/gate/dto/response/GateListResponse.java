package kr.co.webee.presentation.gate.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.gate.entity.Gate;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "개폐기 전체 목록 조회 response")
public record GateListResponse(
        @Schema(description = "개폐기 총 개수", example = "2")
        int totalCount,

        @ArraySchema(schema = @Schema(implementation = GateSummaryResponse.class))
        List<GateSummaryResponse> gates
) {
    public static GateListResponse from(List<Gate> gates) {
        List<GateSummaryResponse> items = gates.stream()
                .map(GateSummaryResponse::from)
                .toList();

        return GateListResponse.builder()
                .totalCount(gates.size())
                .gates(items)
                .build();
    }

    @Builder
    @Schema(description = "개폐기 목록 각 항목 정보")
    public record GateSummaryResponse(
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

            @Schema(description = "등록 일시", example = "2024-03-15T09:00:00")
            LocalDateTime createdAt
    ) {
        public static GateSummaryResponse from(Gate gate) {
            return GateSummaryResponse.builder()
                    .gateId(gate.getId())
                    .name(gate.getName())
                    .region(gate.getRegion())
                    .location(gate.getLocation())
                    .macAddress(gate.getMacAddress())
                    .memo(gate.getMemo())
                    .isConnected(gate.isConnected())
                    .createdAt(gate.getCreatedAt())
                    .build();
        }
    }
}
