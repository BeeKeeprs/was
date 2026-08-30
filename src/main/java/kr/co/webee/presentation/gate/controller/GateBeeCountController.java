package kr.co.webee.presentation.gate.controller;

import kr.co.webee.application.gate.dto.response.GateBeeCountResponse;
import kr.co.webee.application.gate.service.GateBeeCountService;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.presentation.gate.api.GateBeeCountApi;
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
public class GateBeeCountController implements GateBeeCountApi {
    private final GateBeeCountService gateBeeCountService;

    @Override
    @GetMapping("/{gateId}/bee-count")
    public GateBeeCountResponse getBeeCount(
            @PathVariable Long gateId,
            @UserId Long userId,
            @RequestParam Period period
    ) {
        return gateBeeCountService.getBeeCount(gateId, userId, period);
    }
}
