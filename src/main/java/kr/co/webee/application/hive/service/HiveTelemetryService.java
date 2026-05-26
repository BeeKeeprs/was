package kr.co.webee.application.hive.service;

import kr.co.webee.application.hive.dto.HiveTelemetryRequest;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.hive.entity.HiveTelemetry;
import kr.co.webee.domain.hive.repository.HiveRepository;
import kr.co.webee.domain.hive.repository.HiveTelemetryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveTelemetryService {
    private final HiveRepository hiveRepository;
    private final HiveTelemetryRepository hiveTelemetryRepository;

    @Transactional
    public void recordTelemetry(HiveTelemetryRequest request, String macAddress) {
        Hive hive = hiveRepository.findByMacAddress(macAddress)
                .orElseThrow();

        HiveTelemetry telemetry = request.toEntity(hive);

        hiveTelemetryRepository.save(telemetry);
    }
}
