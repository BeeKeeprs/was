package kr.co.webee.application.hive.dto.response;

import lombok.Builder;

@Builder
public record HiveControlScheduleRegisterResponse(
        Long scheduleId
) {
    public static HiveControlScheduleRegisterResponse of(Long scheduleId) {
        return HiveControlScheduleRegisterResponse.builder()
                .scheduleId(scheduleId)
                .build();
    }
}
