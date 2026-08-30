package kr.co.webee.application.gate.service;

import kr.co.webee.application.gate.dto.request.GateBeeCountRequest;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.gate.repository.GateBeeCountRepository;
import kr.co.webee.domain.gate.repository.GateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GateBeeCountService {
    private final GateRepository gateRepository;
    private final GateBeeCountRepository gateBeeCountRepository;

    @Transactional
    public void recordBeeCount(GateBeeCountRequest request, String macAddress) {
        Gate gate = gateRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 개폐기입니다. macAddress=" + macAddress));

        gateBeeCountRepository.save(request.toEntity(gate));
    }
}
