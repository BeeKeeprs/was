package kr.co.webee.application.feedback.service;

import kr.co.webee.infrastructure.mail.MailService;
import kr.co.webee.presentation.feedback.dto.request.FeedbackSubmitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final MailService mailService;

    public void submitFeedback(FeedbackSubmitRequest request) {
        String content = """
                이름: %s
                전화번호: %s
                이메일: %s
                소속: %s
                관심 제품: %s

                문의 내용:
                %s
                """.formatted(request.name(), getDisplayValue(request.phone()), getDisplayValue(request.email()),
                getDisplayValue(request.organization()), request.interestProduct().getValue(), request.message());
        mailService.sendEmail("webee 사용자 피드백", content);
    }

    private String getDisplayValue(String value) {
        return value == null || value.isBlank() ? "미입력" : value;
    }
}
