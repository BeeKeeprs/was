package kr.co.webee.presentation.gate.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.gate.dto.request.GateTimeCardRequest;
import kr.co.webee.application.gate.dto.response.GateTimeCardResponse;
import kr.co.webee.application.gate.service.GateTimeCardService;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/gates")
@RequiredArgsConstructor
@RestController
public class GateTimeCardController {

    private final GateTimeCardService gateTimeCardService;

    @PostMapping("/{gateId}/cards/time")
    @ResponseStatus(HttpStatus.CREATED)
    public GateTimeCardResponse createCard(
            @PathVariable Long gateId,
            @UserId Long userId,
            @RequestBody @Valid GateTimeCardRequest request
    ) {
        return gateTimeCardService.createCard(gateId, userId, request);
    }

    @GetMapping("/{gateId}/cards/time")
    public List<GateTimeCardResponse> getCards(
            @PathVariable Long gateId,
            @UserId Long userId
    ) {
        return gateTimeCardService.getCards(gateId, userId);
    }

    @PutMapping("/{gateId}/cards/time/{cardId}")
    public GateTimeCardResponse updateCard(
            @PathVariable Long gateId,
            @PathVariable Long cardId,
            @UserId Long userId,
            @RequestBody @Valid GateTimeCardRequest request
    ) {
        return gateTimeCardService.updateCard(gateId, userId, cardId, request);
    }

    @DeleteMapping("/{gateId}/cards/time/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(
            @PathVariable Long gateId,
            @PathVariable Long cardId,
            @UserId Long userId
    ) {
        gateTimeCardService.deleteCard(gateId, userId, cardId);
    }
}
