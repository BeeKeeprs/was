package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.HiveControlScheduleRegisterEvent;
import kr.co.webee.domain.hive.entity.HiveControlSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.*;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class HiveControlScheduleEventListener {
    private final HiveControlScheduleService hiveControlScheduleService;
    private final HiveControlScheduleTaskRegistry taskRegistry;
    private final TaskScheduler taskScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void initHiveControlSchedule() {
        List<HiveControlSchedule> schedules = hiveControlScheduleService.findAllEnabledSchedules();

        schedules.forEach(schedule -> {
            registerSchedule(schedule, schedule.getStartTime(), true);
            registerSchedule(schedule, schedule.getEndTime(), false);
        });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerHiveControlSchedule(HiveControlScheduleRegisterEvent event) {
        HiveControlSchedule schedule = event.hiveControlSchedule();

        registerSchedule(schedule, schedule.getStartTime(), true);
        registerSchedule(schedule, schedule.getEndTime(), false);
    }

    private void registerSchedule(HiveControlSchedule schedule, LocalTime localTime, boolean enabled) {
        Long scheduleId = schedule.getId();
        Instant time = toInstant(localTime);

        log.info("자동제어 스케줄링 추가. hiveId: {}, scheduleId={}, enabled: {}, time: {}", schedule.getHive().getId(), scheduleId, enabled, time);

        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> hiveControlScheduleService.publishAutoControlCommand(scheduleId, enabled), time);

        taskRegistry.register(scheduleId, future);
    }

    private Instant toInstant(LocalTime localTime) {
        LocalDateTime dateTime = LocalDate.now().atTime(localTime);

        if (dateTime.isBefore(LocalDateTime.now())) { // 예약 시간이 현재 시각보다 이른 상태에서 작업을 예약하면 즉시 실행되므로 이를 막기위해 익일 시간으로 설정
            dateTime = dateTime.plusDays(1);
        }

        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}