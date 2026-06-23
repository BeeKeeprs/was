package kr.co.webee.presentation.hive.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.hive.dto.request.HiveGateActionRegisterRequest;
import kr.co.webee.application.hive.dto.response.HiveGateActionRegisterResponse;
import kr.co.webee.application.hive.service.HiveGateActionService;
import kr.co.webee.presentation.hive.api.HiveGateActionApi;
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
}
