package kr.co.webee.presentation.market.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.market.service.InterestMarketService;
import kr.co.webee.presentation.market.api.InterestMarketApi;
import kr.co.webee.presentation.market.dto.request.InterestMarketRegisterRequest;
import kr.co.webee.presentation.market.dto.response.InterestMarketRegisterResponse;
import kr.co.webee.presentation.market.dto.response.InterestMarketResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/interest-markets")
@RequiredArgsConstructor
@RestController
public class InterestMarketController implements InterestMarketApi {
    private final InterestMarketService interestMarketService;

    @Override
    @PostMapping
    public InterestMarketRegisterResponse registerInterestMarket(
            @RequestBody @Valid InterestMarketRegisterRequest request,
            @UserId Long userId
    ) {
        return interestMarketService.registerInterestMarket(request, userId);
    }

    @Override
    @GetMapping
    public List<InterestMarketResponse> getAllInterestMarkets(
            @UserId Long userId
    ) {
        return interestMarketService.getAllInterestMarkets(userId);
    }

    @Override
    @DeleteMapping("/{interestMarketId}")
    public String removeInterestMarket(
            @PathVariable Long interestMarketId,
            @UserId Long userId
    ) {
        interestMarketService.removeInterestMarket(interestMarketId, userId);
        return "OK";
    }
}

