package kr.co.webee.presentation.hive.controller;

import kr.co.webee.application.hive.service.HiveTelemetryService;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.domain.hive.type.SensorType;
import kr.co.webee.presentation.hive.api.HiveTelemetryApi;
import kr.co.webee.application.hive.dto.HiveTelemetryResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/hives")
@RequiredArgsConstructor
@RestController
public class HiveTelemetryController implements HiveTelemetryApi {
    private final HiveTelemetryService hiveTelemetryService;

    @Override
    @GetMapping("/{hiveId}/telemetry")
    public HiveTelemetryResponse getTelemetry(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @RequestParam Period period,
            @RequestParam SensorType sensorType
    ) {
        return hiveTelemetryService.getTelemetry(hiveId, userId, period, sensorType);
    }
}
