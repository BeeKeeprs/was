package kr.co.webee.presentation.hive.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
import kr.co.webee.application.hive.dto.request.HiveGateActionUpdateRequest;
import kr.co.webee.application.hive.dto.response.HiveGateActionDetailResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionListResponse;
import kr.co.webee.application.hive.dto.response.HiveGateActionRegisterResponse;
import kr.co.webee.application.hive.service.HiveGateActionService;
import kr.co.webee.presentation.hive.api.HiveGateActionApi;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/hives")
@RequiredArgsConstructor
@RestController
public class HiveGateActionController implements HiveGateActionApi {

    private final HiveGateActionService hiveGateActionService;

    @Override
    @PostMapping("/{hiveId}/gate/actions")
    public HiveGateActionRegisterResponse registerHiveGateAction(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @RequestBody @Valid HiveGateActionRegisterRequest request
    ) {
        return hiveGateActionService.registerHiveGateAction(hiveId, userId, request);
    }

    @Override
    @GetMapping("/{hiveId}/gate/actions")
    public List<HiveGateActionListResponse> getAllHiveGateActionList(
            @PathVariable Long hiveId,
            @UserId Long userId
    ) {
        return hiveGateActionService.getAllHiveGateActionList(hiveId, userId);
    }

    @Override
    @GetMapping("/{hiveId}/gate/actions/{actionId}")
    public HiveGateActionDetailResponse getHiveGateAction(
            @PathVariable Long hiveId,
            @PathVariable Long actionId,
            @UserId Long userId
    ) {
        return hiveGateActionService.getHiveGateAction(hiveId, userId, actionId);
    }

    @Override
    @PatchMapping("/{hiveId}/gate/actions/{actionId}")
    public HiveGateActionDetailResponse updateHiveGateAction(
            @PathVariable Long hiveId,
            @PathVariable Long actionId,
            @UserId Long userId,
            @RequestBody @Valid HiveGateActionUpdateRequest request
    ) {
        return hiveGateActionService.updateHiveGateAction(hiveId, userId, actionId, request);
    }

    @Override
    @DeleteMapping("/{hiveId}/gate/actions/{actionId}")
    public String deleteHiveGateAction(
            @PathVariable Long hiveId,
            @PathVariable Long actionId,
            @UserId Long userId
    ) {
        hiveGateActionService.deleteHiveGateAction(hiveId, userId, actionId);
        return "OK";
    }
}
