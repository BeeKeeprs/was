package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateTelemetryRequest;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.repository.GateRepository;
import kr.co.webee.domain.gate.repository.GateTelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GateTelemetryService {
    private final GateRepository gateRepository;
    private final GateTelemetryRepository gateTelemetryRepository;

    @Transactional
    public void recordTelemetry(GateTelemetryRequest request, String macAddress) {
        Gate gate = gateRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 개폐기입니다. macAddress=" + macAddress));

        gateTelemetryRepository.save(request.toEntity(gate));
    }
}
