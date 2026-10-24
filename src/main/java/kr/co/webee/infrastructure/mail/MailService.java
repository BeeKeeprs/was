package kr.co.webee.infrastructure.mail;

import jakarta.mail.internet.MimeMessage;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender mailSender;

    public void sendEmail(String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, false);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("이메일 전송중에 오류가 발생하였습니다 {}", e.getMessage(), e);
            throw new BusinessException(ErrorType.UNHANDLED_EXCEPTION);
        }
    }
}
