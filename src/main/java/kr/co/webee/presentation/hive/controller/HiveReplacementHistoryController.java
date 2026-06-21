package kr.co.webee.presentation.hive.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.hive.service.HiveReplacementHistoryService;
import kr.co.webee.presentation.hive.api.HiveReplacementHistoryApi;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryCreateRequest;
import kr.co.webee.presentation.hive.dto.request.HiveReplacementHistoryUpdateRequest;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryCreateResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryDetailResponse;
import kr.co.webee.presentation.hive.dto.response.HiveReplacementHistoryListResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @Override
    @GetMapping("/{hiveId}/replacement-history/{historyId}")
    public HiveReplacementHistoryDetailResponse getReplacementHistoryDetail(
            @PathVariable Long hiveId,
            @PathVariable Long historyId,
            @UserId Long userId
    ) {
        return hiveReplacementHistoryService.getReplacementHistoryDetail(hiveId, historyId, userId);
    }

    @Override
    @GetMapping("/{hiveId}/replacement-history")
    public Slice<HiveReplacementHistoryListResponse> getAllReplacementHistories(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @PageableDefault(size = 10, sort = "replacedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return hiveReplacementHistoryService.getAllReplacementHistories(hiveId, userId, pageable);
    }

    @Override
    @PatchMapping("/{hiveId}/replacement-history/{historyId}")
    public void updateReplacementHistory(
            @PathVariable Long hiveId,
            @PathVariable Long historyId,
            @UserId Long userId,
            @RequestBody @Valid HiveReplacementHistoryUpdateRequest request
    ) {
        hiveReplacementHistoryService.updateReplacementHistory(hiveId, historyId, userId, request);
    }
}
