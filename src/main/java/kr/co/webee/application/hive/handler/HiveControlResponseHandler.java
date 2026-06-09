package kr.co.webee.application.hive.handler;

import kr.co.webee.application.hive.dto.response.HiveControlCommandResponse;
import kr.co.webee.application.hive.dto.response.HiveAutoControlCommandProcessResponse;
import kr.co.webee.application.hive.dto.HivePendingCommand;
import kr.co.webee.application.hive.service.HiveControlService;
import kr.co.webee.application.mqtt.MqttMessageHandler;
import kr.co.webee.application.mqtt.MqttTopicType;
import kr.co.webee.application.sse.service.SseEmitterService;
import kr.co.webee.application.sse.type.SseEventType;
import kr.co.webee.common.util.JsonConverter;
import kr.co.webee.infrastructure.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class HiveControlResponseHandler implements MqttMessageHandler {
    private final HiveControlService hiveControlService;
    private final SseEmitterService sseEmitterService;
    private final RedisService redisService;
    private final JsonConverter jsonConverter;

    @Override
    public MqttTopicType getTopicType() {
        return MqttTopicType.CONTROL_RESPONSE;
    }

    @Override
    public void handle(Object payload, String macAddress) {
        HiveControlCommandResponse response = jsonConverter.convert(payload, HiveControlCommandResponse.class);

        String redisKey = HivePendingCommand.redisKey(response.commandId());
        Object stored = redisService.get(redisKey);

        if (stored == null) {
            log.warn("commandId={}에 해당하는 대기 명령이 없습니다 (만료 또는 미존재)", response.commandId());
            return;
        }

        redisService.delete(redisKey);

        HivePendingCommand pending = jsonConverter.convert(stored, HivePendingCommand.class);
        HiveAutoControlCommandProcessResponse result = hiveControlService.processAutoControlCommandResponse(pending, response);

        sseEmitterService.sendToClient(SseEventType.HIVE_CONTROL_RESULT, pending.userId(), result);
    }
}