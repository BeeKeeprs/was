package kr.co.webee.application.gate.service;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.repository.GateBeeCountRepository;
import kr.co.webee.domain.gate.repository.GateCommandRepository;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.gate.repository.GateTelemetryRepository;
import kr.co.webee.domain.user.entity.User;
import kr.co.webee.domain.user.repository.UserRepository;
import kr.co.webee.presentation.gate.dto.request.GateRegisterRequest;
import kr.co.webee.presentation.gate.dto.request.GateUpdateRequest;
import kr.co.webee.presentation.gate.dto.response.GateDetailResponse;
import kr.co.webee.presentation.gate.dto.response.GateListResponse;
import kr.co.webee.presentation.gate.dto.response.GateRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GateService {
    private final GateRepository gateRepository;
    private final UserRepository userRepository;
    private final GateTelemetryRepository gateTelemetryRepository;
    private final GateBeeCountRepository gateBeeCountRepository;
    private final GateCommandRepository gateCommandRepository;

    @Transactional
    public GateRegisterResponse registerGate(GateRegisterRequest request, Long userId) {
        if (gateRepository.existsByMacAddress(request.macAddress())) {
            throw new BusinessException(ErrorType.GATE_MAC_ADDRESS_ALREADY_EXISTS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Gate gate = gateRepository.save(request.toEntity(user));

        return GateRegisterResponse.of(gate.getId());
    }

    @Transactional(readOnly = true)
    public GateListResponse getAllGates(Long userId) {
        List<Gate> gates = gateRepository.findAllByUserId(userId);
        return GateListResponse.from(gates);
    }

    @Transactional(readOnly = true)
    public GateDetailResponse getGateDetail(Long gateId, Long userId) {
        Gate gate = gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));
        return GateDetailResponse.from(gate);
    }

    @Transactional
    public void updateGate(Long gateId, Long userId, GateUpdateRequest request) {
        Gate gate = gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));
        gate.update(request.name(), request.region(), request.location(), request.memo());
    }

    @Transactional
    public void deleteGate(Long gateId, Long userId) {
        Gate gate = gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));

        gateTelemetryRepository.deleteAllByGateId(gateId);
        gateBeeCountRepository.deleteAllByGateId(gateId);
        gateCommandRepository.deleteAllByGateId(gateId);
        gateRepository.delete(gate);
    }
}
