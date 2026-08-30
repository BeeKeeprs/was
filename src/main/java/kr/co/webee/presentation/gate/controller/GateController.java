package kr.co.webee.presentation.gate.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.gate.service.GateService;
import kr.co.webee.presentation.gate.api.GateApi;
import kr.co.webee.presentation.gate.dto.request.GateRegisterRequest;
import kr.co.webee.presentation.gate.dto.response.GateRegisterResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/gates")
@RequiredArgsConstructor
@RestController
public class GateController implements GateApi {
    private final GateService gateService;

    @Override
    @PostMapping
    public GateRegisterResponse registerGate(@RequestBody @Valid GateRegisterRequest request, @UserId Long userId) {
        return gateService.registerGate(request, userId);
    }
}
