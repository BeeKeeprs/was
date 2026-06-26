package kr.co.webee.presentation.interestpesticide.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.interestpesticide.service.InterestPesticideService;
import kr.co.webee.presentation.interestpesticide.api.InterestPesticideApi;
import kr.co.webee.presentation.interestpesticide.dto.request.InterestPesticideRegisterRequest;
import kr.co.webee.presentation.interestpesticide.dto.response.InterestPesticideRegisterResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/interest-pesticides")
@RequiredArgsConstructor
@RestController
public class InterestPesticideController implements InterestPesticideApi {
    private final InterestPesticideService interestPesticideService;

    @Override
    @PostMapping
    public InterestPesticideRegisterResponse registerInterestPesticide(
            @RequestBody @Valid InterestPesticideRegisterRequest request,
            @UserId Long userId
    ) {
        return interestPesticideService.registerInterestPesticide(request, userId);
    }
}
