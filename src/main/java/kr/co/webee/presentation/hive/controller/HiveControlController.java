package kr.co.webee.presentation.hive.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.hive.service.HiveControlService;
import kr.co.webee.presentation.hive.api.HiveControlApi;
import kr.co.webee.presentation.hive.dto.request.HiveAutoControlRequest;
import kr.co.webee.presentation.hive.dto.request.HiveManualControlRequest;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/hives")
@RequiredArgsConstructor
@RestController
public class HiveControlController implements HiveControlApi {
    private final HiveControlService hiveControlService;

    @Override
    @PostMapping("/{hiveId}/control/auto")
    public String setAutoControl(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @RequestBody @Valid HiveAutoControlRequest request
    ) {
        hiveControlService.setAutoControl(hiveId, userId, request);
        return "OK";
    }

    @Override
    @PostMapping("/{hiveId}/control/manual")
    public String setManualControl(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @RequestBody @Valid HiveManualControlRequest request
    ) {
        hiveControlService.setManualControl(hiveId, userId, request);
        return "OK";
    }
}
