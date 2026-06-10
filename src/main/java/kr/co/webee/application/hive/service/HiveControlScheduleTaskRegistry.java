package kr.co.webee.application.hive.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class HiveControlScheduleTaskRegistry {
    private final Map<Long, List<ScheduledFuture<?>>> tasks = new ConcurrentHashMap<>();

    public void register(Long scheduleId, ScheduledFuture<?> future) {
        tasks.computeIfAbsent(scheduleId, k -> new ArrayList<>()).add(future);
    }

    public void cancel(Long scheduleId) {
        List<ScheduledFuture<?>> futures = tasks.remove(scheduleId);

        if (futures != null) {
            futures.forEach(f -> f.cancel(false));
        }
    }
}
