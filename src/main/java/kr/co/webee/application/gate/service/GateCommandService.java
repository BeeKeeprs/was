package kr.co.webee.application.gate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.webee.application.gate.dto.GateCommandResultMessage;
import kr.co.webee.application.gate.dto.request.GateCommandRequest;
import kr.co.webee.application.gate.dto.response.GateCommandAcceptedResponse;
import kr.co.webee.application.gate.dto.response.GateCommandStatusResponse;
import kr.co.webee.application.gate.dto.response.GateCurrentCommandResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.entity.GateCommand;
import kr.co.webee.domain.gate.repository.GateCommandRepository;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.gate.type.GateCardType;
import kr.co.webee.domain.gate.type.GateCommandOperation;
import kr.co.webee.domain.gate.type.GateCommandStatus;
import kr.co.webee.domain.gate.type.GateExecutionStatus;
import kr.co.webee.infrastructure.mqtt.config.MqttBrokerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GateCommandService {

    private static final int TIMEOUT_SECONDS = 15;

    private final GateRepository gateRepository;
    private final GateCommandRepository gateCommandRepository;
    private final MqttBrokerConfig.MqttPublisher mqttPublisher;
    private final ObjectMapper objectMapper;

    @Transactional
    public GateCommandAcceptedResponse issueCommand(Long gateId, Long userId, GateCommandRequest request) {
        Gate gate = gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));

        if (!gate.getMacAddress().equals(request.gateId())) {
            throw new BusinessException(ErrorType.GATE_NOT_FOUND);
        }

        if (!gate.isConnected()) {
            throw new BusinessException(ErrorType.GATE_OFFLINE);
        }

        GateCommandOperation operation = request.resolvedOperation();
        String commandId = UUID.randomUUID().toString();
        GateCommand command;

        if (operation == GateCommandOperation.CANCEL) {
            validateCancelRequest(request);
            command = gateCommandRepository.save(GateCommand.builder()
                    .id(commandId)
                    .gate(gate)
                    .operation(GateCommandOperation.CANCEL)
                    .targetCommandId(request.targetCommandId())
                    .build());
        } else {
            validateExecuteRequest(request);
            String payloadJson = serializePayload(request.payload());
            command = gateCommandRepository.save(GateCommand.builder()
                    .id(commandId)
                    .gate(gate)
                    .operation(GateCommandOperation.EXECUTE)
                    .cardType(request.cardType())
                    .title(request.title())
                    .memo(request.memo())
                    .payloadJson(payloadJson)
                    .build());
        }

        publishToMqtt(gate.getMacAddress(), commandId, operation, request);

        return GateCommandAcceptedResponse.from(command);
    }

    @Transactional(readOnly = true)
    public GateCommandStatusResponse getCommandStatus(Long gateId, Long userId, String commandId) {
        gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));

        GateCommand command = gateCommandRepository.findByIdAndGateId(commandId, gateId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_COMMAND_NOT_FOUND));

        return GateCommandStatusResponse.from(command);
    }

    @Transactional(readOnly = true)
    public GateCurrentCommandResponse getCurrentCommand(Long gateId, Long userId) {
        gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));

        return gateCommandRepository.findByGateIdAndExecutionStatus(gateId, GateExecutionStatus.ACTIVE)
                .map(command -> GateCurrentCommandResponse.from(command, parsePayload(command.getPayloadJson())))
                .orElse(null);
    }

    @Transactional
    public void handleResult(GateCommandResultMessage result) {
        gateCommandRepository.findById(result.commandId()).ifPresentOrElse(
                command -> {
                    command.complete(result.status(), result.detail());

                    if ("OK".equals(result.status()) && result.executionCommandId() != null) {
                        if ("ACTIVE".equals(result.executionStatus())) {
                            command.activate();
                        } else if ("CANCELLED".equals(result.executionStatus())) {
                            gateCommandRepository.findById(result.executionCommandId())
                                    .ifPresent(GateCommand::cancel);
                        }
                    }
                },
                () -> log.warn("결과에 해당하는 명령 없음 commandId={}", result.commandId())
        );
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void timeoutPendingCommands() {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(TIMEOUT_SECONDS);
        List<GateCommand> pending = gateCommandRepository
                .findAllByStatusAndCreatedAtBefore(GateCommandStatus.PENDING, threshold);

        pending.forEach(command -> {
            command.timeout();
            log.info("개폐기 명령 타임아웃 commandId={}", command.getId());
        });
    }

    private void validateExecuteRequest(GateCommandRequest request) {
        if (request.cardType() == null) {
            throw new BusinessException(ErrorType.FAILED_VALIDATION);
        }
        if (request.title() == null || request.title().isBlank()) {
            throw new BusinessException(ErrorType.FAILED_VALIDATION);
        }
        validatePayload(request.cardType(), request.payload());
    }

    private void validateCancelRequest(GateCommandRequest request) {
        if (request.targetCommandId() == null || request.targetCommandId().isBlank()) {
            throw new BusinessException(ErrorType.FAILED_VALIDATION);
        }
    }

    private void validatePayload(GateCardType cardType, JsonNode payload) {
        switch (cardType) {
            case OPEN_NOW, CLOSE_NOW, COUNT_STATUS -> { /* payload 없음, 무시 */ }
            case OPEN_AT -> {
                requireTimeField(payload, "start");
            }
            case CLOSE_AT -> {
                requireTimeField(payload, "end");
            }
            case WINDOW -> {
                requireTimeField(payload, "start");
                requireTimeField(payload, "end");
            }
            case ALTERNATE_24H -> {
                requireLiteralField(payload, "start", "close_first", "open_first");
            }
            case COUNT_CONTROL -> {
                requireField(payload, "countRange");
                requireField(payload, "timeWindow");
                requireField(payload, "rules");
            }
        }
    }

    private void requireField(JsonNode payload, String field) {
        if (payload == null || payload.isNull() || !payload.has(field)) {
            throw new BusinessException(ErrorType.GATE_COMMAND_INVALID_PAYLOAD);
        }
    }

    private void requireTimeField(JsonNode payload, String field) {
        requireField(payload, field);
        String value = payload.get(field).asText();
        if (!value.matches("^([01]\\d|2[0-4]):[0-5]\\d$")) {
            throw new BusinessException(ErrorType.GATE_COMMAND_INVALID_PAYLOAD);
        }
    }

    private void requireLiteralField(JsonNode payload, String field, String... allowed) {
        requireField(payload, field);
        String value = payload.get(field).asText();
        for (String allow : allowed) {
            if (allow.equals(value)) return;
        }
        throw new BusinessException(ErrorType.GATE_COMMAND_INVALID_PAYLOAD);
    }

    private String serializePayload(JsonNode payload) {
        if (payload == null || payload.isNull()) return null;
        return payload.toString();
    }

    private JsonNode parsePayload(String payloadJson) {
        if (payloadJson == null) return null;
        try {
            return objectMapper.readTree(payloadJson);
        } catch (Exception e) {
            log.warn("payloadJson 파싱 실패: {}", payloadJson, e);
            return null;
        }
    }

    private void publishToMqtt(String macAddress, String commandId, GateCommandOperation operation,
                               GateCommandRequest request) {
        try {
            ObjectNode mqttPayload = objectMapper.createObjectNode();
            mqttPayload.put("commandId", commandId);
            mqttPayload.put("gateId", macAddress);
            mqttPayload.put("operation", operation.name());

            if (operation == GateCommandOperation.CANCEL) {
                mqttPayload.put("targetCommandId", request.targetCommandId());
            } else {
                mqttPayload.put("cardType", request.cardType().name());
                if (request.payload() != null && !request.payload().isNull()) {
                    mqttPayload.set("payload", request.payload());
                }
            }

            String topic = "gate/%s/command".formatted(macAddress);
            mqttPublisher.publish(topic, mqttPayload.toString());
        } catch (Exception e) {
            log.error("MQTT publish 실패 commandId={}", commandId, e);
        }
    }
}
