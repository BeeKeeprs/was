package kr.co.webee.presentation.interestnewskeyword.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.interestnewskeyword.service.InterestNewsKeywordService;
import kr.co.webee.presentation.interestnewskeyword.api.InterestNewsKeywordApi;
import kr.co.webee.presentation.interestnewskeyword.dto.request.InterestNewsKeywordRegisterRequest;
import kr.co.webee.presentation.interestnewskeyword.dto.response.InterestNewsKeywordRegisterResponse;
import kr.co.webee.presentation.interestnewskeyword.dto.response.InterestNewsKeywordResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interest-news-keywords")
public class InterestNewsKeywordController implements InterestNewsKeywordApi {
    private final InterestNewsKeywordService interestNewsKeywordService;

    @Override
    @PostMapping
    public InterestNewsKeywordRegisterResponse registerInterestNewsKeyword(
            @RequestBody @Valid InterestNewsKeywordRegisterRequest request,
            @UserId Long userId
    ) {
        return interestNewsKeywordService.registerInterestNewsKeyword(request, userId);
    }

    @Override
    @GetMapping
    public List<InterestNewsKeywordResponse> getInterestNewsKeywords(@UserId Long userId) {
        return interestNewsKeywordService.getInterestNewsKeywords(userId);
    }
}

