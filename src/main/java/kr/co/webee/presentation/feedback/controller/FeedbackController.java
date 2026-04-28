package kr.co.webee.presentation.feedback.controller;

import jakarta.validation.Valid;
import kr.co.webee.application.feedback.service.FeedbackService;
import kr.co.webee.presentation.feedback.api.FeedbackApi;
import kr.co.webee.presentation.feedback.dto.FeedbackSubmitRequest;
import kr.co.webee.presentation.support.annotation.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/suggestions")
@RestController
public class FeedbackController implements FeedbackApi {
    private final FeedbackService feedbackService;

    @Override
    @PostMapping
    public String submitFeedback(@RequestBody @Valid FeedbackSubmitRequest request, @UserId Long userId) {
        feedbackService.submitFeedback(request, userId);
        return "OK";
    }
}
