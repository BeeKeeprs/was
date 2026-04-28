package kr.co.webee.application.feedback.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.infrastructure.mail.MailService;
import kr.co.webee.presentation.feedback.dto.FeedbackSubmitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackService {
    private final MailService mailService;
    private final UserRepository userRepository;

    public void submitFeedback(FeedbackSubmitRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        mailService.sendEmail(request.content(), user.getName());
    }
}
