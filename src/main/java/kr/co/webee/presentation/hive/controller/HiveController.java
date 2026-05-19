package kr.co.webee.presentation.hive.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.hive.service.HiveService;
import kr.co.webee.presentation.hive.api.HiveApi;
import kr.co.webee.presentation.hive.dto.request.HiveRegisterRequest;
import kr.co.webee.presentation.hive.dto.response.HiveDetailResponse;
import kr.co.webee.presentation.hive.dto.response.HiveListResponse;
import kr.co.webee.presentation.hive.dto.response.HiveRegisterResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/hives")
@RequiredArgsConstructor
@RestController
public class HiveController implements HiveApi {
    private final HiveService hiveService;

    @Override
    @PostMapping
    public HiveRegisterResponse registerHive(
            @RequestBody @Valid HiveRegisterRequest request,
            @UserId Long userId
    ) {
        return hiveService.registerHive(request, userId);
    }

    @Override
    @GetMapping
    public HiveListResponse getAllHives(@UserId Long userId) {
        return hiveService.getAllHives(userId);
    }

    @Override
    @GetMapping("/{hiveId}")
    public HiveDetailResponse getHiveDetail(@PathVariable Long hiveId, @UserId Long userId) {
        return hiveService.getHiveDetail(hiveId, userId);
    }

    @Override
    @DeleteMapping("/{hiveId}")
    public void deleteHive(@PathVariable Long hiveId, @UserId Long userId) {
        hiveService.deleteHive(hiveId, userId);
    }
}
