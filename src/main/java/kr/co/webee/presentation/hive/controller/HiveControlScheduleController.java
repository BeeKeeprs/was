package kr.co.webee.presentation.hive.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.hive.dto.request.HiveControlScheduleRegisterRequest;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleListResponse;
import kr.co.webee.application.hive.dto.response.HiveControlScheduleRegisterResponse;
import kr.co.webee.application.hive.service.HiveControlScheduleService;
import kr.co.webee.presentation.hive.api.HiveControlScheduleApi;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/hives")
@RequiredArgsConstructor
@RestController
public class HiveControlScheduleController implements HiveControlScheduleApi {
    private final HiveControlScheduleService hiveControlScheduleService;

    @Override
    @PostMapping("/{hiveId}/control/auto/schedules")
    public HiveControlScheduleRegisterResponse registerHiveControlSchedule(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @RequestBody @Valid HiveControlScheduleRegisterRequest request
    ) {
        return hiveControlScheduleService.registerHiveControlSchedule(hiveId, userId, request);
    }

    @Override
    @GetMapping("/{hiveId}/control/auto/schedules")
    public List<HiveControlScheduleListResponse> getHiveControlScheduleList(
            @PathVariable Long hiveId,
            @UserId Long userId
    ) {
        return hiveControlScheduleService.getHiveControlScheduleList(hiveId, userId);
    }

    @Override
    @DeleteMapping("/{hiveId}/control/auto/schedules/{scheduleId}")
    public String deleteHiveControlSchedule(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @PathVariable Long scheduleId
    ) {
        hiveControlScheduleService.deleteHiveControlSchedule(hiveId, userId, scheduleId);
        return "OK";
    }
}
