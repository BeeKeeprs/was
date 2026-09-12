package kr.co.webee.presentation.gate.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.gate.dto.request.GateCommandRequest;
import kr.co.webee.application.gate.dto.response.GateCommandAcceptedResponse;
import kr.co.webee.application.gate.dto.response.GateCommandStatusResponse;
import kr.co.webee.application.gate.service.GateCommandService;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/gates")
@RequiredArgsConstructor
@RestController
public class GateCommandController {

    private final GateCommandService gateCommandService;

    @PostMapping("/{gateId}/commands")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GateCommandAcceptedResponse issueCommand(
            @PathVariable Long gateId,
            @UserId Long userId,
            @RequestBody @Valid GateCommandRequest request
    ) {
        return gateCommandService.issueCommand(gateId, userId, request);
    }

    @GetMapping("/{gateId}/commands/{commandId}")
    public GateCommandStatusResponse getCommandStatus(
            @PathVariable Long gateId,
            @PathVariable String commandId,
            @UserId Long userId
    ) {
        return gateCommandService.getCommandStatus(gateId, userId, commandId);
    }
}
