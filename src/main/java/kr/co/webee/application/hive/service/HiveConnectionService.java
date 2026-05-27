package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.request.HiveConnectionRequest;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.repository.HiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HiveConnectionService {
    private final HiveRepository hiveRepository;

    @Transactional
    public void recordConnection(HiveConnectionRequest request, String macAddress) {
        Hive hive = hiveRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 벌통입니다. macAddress=" + macAddress));

        hive.updateConnection(request.isConnected());
    }
}
