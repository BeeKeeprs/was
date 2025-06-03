package kr.co.webee.application.bee.diagnosis;

import kr.co.webee.infrastructure.bee.diagnosis.client.BeeDiseaseDetectClient;
import kr.co.webee.infrastructure.bee.diagnosis.dto.BeeDiseaseDetectResultResponse;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiagnosisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BeeDiagnosisService {
    private final BeeDiseaseDetectClient beeDiseaseDetectClient;

    public BeeDiagnosisResponse diagnoseBeeDisease(MultipartFile image) {
        BeeDiseaseDetectResultResponse.Prediction prediction = beeDiseaseDetectClient.detect(image);
        return BeeDiagnosisResponse.from(prediction.type(), prediction.confidence());
    }
}
