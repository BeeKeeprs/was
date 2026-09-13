package kr.co.webee.presentation.gate.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.gate.dto.request.GateCountCardRequest;
import kr.co.webee.application.gate.dto.response.GateCountCardResponse;
import kr.co.webee.application.gate.service.GateCountCardService;
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

@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@RestController
public class GateCountCardController {

    private final GateCountCardService gateCountCardService;

    @PostMapping("/count")
    @ResponseStatus(HttpStatus.CREATED)
    public GateCountCardResponse createCard(
            @UserId Long userId,
            @RequestBody @Valid GateCountCardRequest request
    ) {
        return gateCountCardService.createCard(userId, request);
    }

    @GetMapping("/count")
    public List<GateCountCardResponse> getCards(
            @UserId Long userId
    ) {
        return gateCountCardService.getCards(userId);
    }

    @PutMapping("/count/{cardId}")
    public GateCountCardResponse updateCard(
            @PathVariable Long cardId,
            @UserId Long userId,
            @RequestBody @Valid GateCountCardRequest request
    ) {
        return gateCountCardService.updateCard(userId, cardId, request);
    }

    @DeleteMapping("/count/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(
            @PathVariable Long cardId,
            @UserId Long userId
    ) {
        gateCountCardService.deleteCard(userId, cardId);
    }
}
