package kr.co.webee.presentation.hive.controller;

import kr.co.webee.application.hive.dto.response.HiveBeeCountResponse;
import kr.co.webee.application.hive.service.HiveBeeCountService;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.presentation.hive.api.HiveBeeCountApi;
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
public class HiveBeeCountController implements HiveBeeCountApi {
    private final HiveBeeCountService hiveBeeCountService;

    @Override
    @GetMapping("/{hiveId}/bee-count")
    public HiveBeeCountResponse getBeeCount(
            @PathVariable Long hiveId,
            @UserId Long userId,
            @RequestParam Period period
    ) {
        return hiveBeeCountService.getBeeCount(hiveId, userId, period);
    }
}
