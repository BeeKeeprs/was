package kr.co.webee.application.hive.dto;

import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import lombok.Builder;


@Builder
public record HiveControlScheduleRegisterEvent(
        HiveControlSchedule hiveControlSchedule
) {
    public static HiveControlScheduleRegisterEvent from(HiveControlSchedule hiveControlSchedule) {
        return HiveControlScheduleRegisterEvent.builder()
                .hiveControlSchedule(hiveControlSchedule)
                .build();
    }
}
