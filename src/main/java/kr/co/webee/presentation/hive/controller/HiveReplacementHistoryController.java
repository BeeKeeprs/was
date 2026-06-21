package kr.co.webee.presentation.hive.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.hive.service.HiveReplacementHistoryService;
import kr.co.webee.presentation.hive.api.HiveReplacementHistoryApi;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryCreateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryCreateResponse;
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
public class HiveReplacementHistoryController implements HiveReplacementHistoryApi {
    private final HiveReplacementHistoryService hiveReplacementHistoryService;

    @Override
    @PostMapping("/{hiveId}/replacement-history")
    public HiveReplacementHistoryCreateResponse createReplacementHistory(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @RequestBody @Valid HiveReplacementHistoryCreateRequest request
    ) {
        return hiveReplacementHistoryService.createReplacementHistory(hiveId, userId, request);
    }
}
