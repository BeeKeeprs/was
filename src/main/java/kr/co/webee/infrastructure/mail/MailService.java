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
    private static final String title = "webee 사용자 피드백";
    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender mailSender;

    public void sendEmail(String content, String name, String userEmail) {
        String body = "<h3>" + name + "님이 주신 문의입니다.</h3><p>이메일: " + userEmail + "</p><p>" + content + "</p>";
        sendMail(title, body, true);
    }

    public void sendEmail(String subject, String content) {
        sendMail(subject, content, false);
    }

    private void sendMail(String subject, String content, boolean html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, html);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("이메일 전송중에 오류가 발생하였습니다 {}", e.getMessage(), e);
            throw new BusinessException(ErrorType.UNHANDLED_EXCEPTION);
        }
    }
}
