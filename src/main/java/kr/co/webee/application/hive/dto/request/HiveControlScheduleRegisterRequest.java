package kr.co.webee.application.hive.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveControlSchedule;

import java.time.LocalTime;

@Schema(description = "벌통 센서 자동제어 스케줄 등록 request")
public record HiveControlScheduleRegisterRequest(
        @Schema(description = "자동제어 시작 시각", example = "08:00:00")
        @NotNull
        LocalTime startTime,

        @Schema(description = "자동제어 종료 시각", example = "18:00:00")
        @NotNull
        LocalTime endTime
) {
    public HiveControlSchedule toEntity(Hive hive) {
        return HiveControlSchedule.builder()
                .hive(hive)
                .startTime(startTime)
                .endTime(endTime)
                .enabled(true)
                .build();
    }

    @Hidden
    public boolean isValidTimeRange() {
        return startTime.isBefore(endTime);
    }
}