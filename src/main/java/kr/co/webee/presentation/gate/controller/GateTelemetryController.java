package kr.co.webee.presentation.gate.controller;

import kr.co.webee.application.gate.dto.response.GateTelemetryResponse;
import kr.co.webee.application.gate.service.GateTelemetryService;
import kr.co.webee.domain.gate.type.GateSensorType;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.presentation.gate.api.GateTelemetryApi;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/gates")
@RequiredArgsConstructor
@RestController
public class GateTelemetryController implements GateTelemetryApi {
    private final GateTelemetryService gateTelemetryService;

    @Override
    @GetMapping("/{gateId}/telemetry")
    public GateTelemetryResponse getTelemetry(
            @PathVariable Long gateId,
            @UserId Long userId,
            @RequestParam Period period,
            @RequestParam GateSensorType sensorType
    ) {
        return gateTelemetryService.getTelemetry(gateId, userId, period, sensorType);
    }
}
