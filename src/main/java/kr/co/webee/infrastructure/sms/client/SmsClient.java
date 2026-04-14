package kr.co.webee.infrastructure.sms.client;

public interface SmsClient {

    void sendMessage(String to, String message);
}
