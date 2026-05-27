package kr.co.webee.presentation.hive.controller;

import kr.co.webee.application.hive.dto.response.HiveConnectionResponse;
import kr.co.webee.application.hive.service.HiveConnectionService;
import kr.co.webee.presentation.hive.api.HiveConnectionApi;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/hives")
@RequiredArgsConstructor
@RestController
public class HiveConnectionController implements HiveConnectionApi {
    private final HiveConnectionService hiveConnectionService;

    @Override
    @GetMapping("/{hiveId}/connection")
    public HiveConnectionResponse getConnection(
            @PathVariable Long hiveId,
            @UserId Long userId
    ) {
        return hiveConnectionService.getConnection(hiveId, userId);
    }
}
