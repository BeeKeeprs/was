package kr.co.webee.infrastructure.sms.client;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SolapiSmsClient implements SmsClient {
    private final DefaultMessageService messageService;
    private final String fromNumber;

    public SolapiSmsClient(
        @Value("${solapi.api-key:}") String apiKey,
        @Value("${solapi.api-secret:}") String apiSecret,
        @Value("${solapi.from-number:}") String fromNumber
    ) {
        this.fromNumber = fromNumber;
        this.messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
    }

    @Override
    public void sendMessage(String to, String message) {
        Message requestMessage = new Message();
        requestMessage.setFrom(fromNumber);
        requestMessage.setTo(to);
        requestMessage.setText(message);

        try {
            this.messageService.send(requestMessage);
        } catch (Exception e) {
            log.error("SOLAPI SMS 전송 실패. to={}", to, e);
            throw new BusinessException(ErrorType.SMS_SEND_FAILED);
        }
    }
}
