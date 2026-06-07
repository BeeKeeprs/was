package kr.co.webee.application.hive.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@Schema(description = "벌통 센서 자동제어 스케줄 목록 각 항목")
public record HiveControlScheduleListResponse(
        @Schema(description = "스케줄 ID", example = "1")
        Long scheduleId,

        @Schema(description = "자동제어 시작 시각", example = "08:00")
        LocalTime startTime,

        @Schema(description = "자동제어 종료 시각", example = "18:00")
        LocalTime endTime
) {
    public static HiveControlScheduleListResponse from(HiveControlSchedule schedule) {
        return HiveControlScheduleListResponse.builder()
                .scheduleId(schedule.getId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
