package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateConnectionRequest;
import kr.co.webee.application.gate.dto.response.GateConnectionResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.repository.GateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GateConnectionService {

    private final GateRepository gateRepository;

    @Transactional
    public void recordConnection(GateConnectionRequest request, String macAddress) {
        Gate gate = gateRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 개폐기입니다. macAddress=" + macAddress));

        gate.updateConnection(request.isConnected());
    }

    @Transactional(readOnly = true)
    public GateConnectionResponse getConnection(Long gateId, Long userId) {
        Gate gate = gateRepository.findByIdAndUserId(gateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorType.GATE_NOT_FOUND));

        return GateConnectionResponse.from(gate);
    }
}
