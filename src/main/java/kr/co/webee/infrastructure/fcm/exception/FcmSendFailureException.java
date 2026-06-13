package kr.co.webee.infrastructure.fcm.exception;

public class FcmSendFailureException extends RuntimeException{
    public FcmSendFailureException(String message) {
        super(message);
    }
}