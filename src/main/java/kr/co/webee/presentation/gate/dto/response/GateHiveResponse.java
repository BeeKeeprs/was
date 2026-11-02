package kr.co.webee.presentation.gate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.entity.Hive;
import lombok.Builder;

@Builder
@Schema(description = "개폐기에 연결된 벌통 정보")
public record GateHiveResponse(
        @Schema(description = "벌통 ID", example = "1")
        Long hiveId,

        @Schema(description = "벌통 이름", example = "딸기 온실 1호 벌통")
        String name
) {
    public static GateHiveResponse from(Hive hive) {
        if (hive == null) {
            return null;
        }
        return GateHiveResponse.builder()
                .hiveId(hive.getId())
                .name(hive.getName())
                .build();
    }
}
