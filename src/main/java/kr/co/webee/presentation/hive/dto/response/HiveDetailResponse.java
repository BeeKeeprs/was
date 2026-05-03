package kr.co.webee.presentation.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.entity.Hive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "벌통 상세 response")
public record HiveDetailResponse(
        @Schema(description = "벌통 ID", example = "1")
        Long hiveId,

        @Schema(description = "벌통 이름", example = "딸기 벌통")
        String name,

        @Schema(description = "지역", example = "충청북도 청주시 오창읍")
        String region,

        @Schema(description = "설치 위치", example = "과수원 남쪽")
        String location,

        @Schema(description = "일련번호", example = "HV-20240315-0001")
        String serialNumber,

        @Schema(description = "메모", example = "월동 후 점검 필요", nullable = true)
        String memo,

        @Schema(description = "등록 일시", example = "2024-03-15T09:00:00")
        LocalDateTime createdAt,

        @Schema(description = "수정 일시", example = "2024-03-16T10:00:00", nullable = true)
        LocalDateTime modifiedAt
) {
    public static HiveDetailResponse from(Hive hive) {
        return HiveDetailResponse.builder()
                .hiveId(hive.getId())
                .name(hive.getName())
                .region(hive.getRegion())
                .location(hive.getLocation())
                .serialNumber(hive.getSerialNumber())
                .memo(hive.getMemo())
                .createdAt(hive.getCreatedAt())
                .modifiedAt(hive.getModifiedAt())
                .build();
    }
}
