package kr.co.webee.application.mqtt;

public interface MqttMessageHandler {

    MqttTopicType getTopicType();

    void handle(Object payload, String macAddress);
}
