package kr.co.webee.presentation.bee.diagnosis.controller;

import kr.co.webee.application.bee.diagnosis.BeeDiagnosisService;
import kr.co.webee.presentation.bee.diagnosis.api.BeeDiagnosisApi;
import kr.co.webee.presentation.bee.diagnosis.dto.BeeDiagnosisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/bee/diagnosis")
@RequiredArgsConstructor
public class BeeDiagnosisController implements BeeDiagnosisApi {
    private final BeeDiagnosisService beeDiagnosisService;

    @PostMapping()
    public BeeDiagnosisResponse diagnoseBeeDisease(@RequestParam(value = "beeImage", required = true) MultipartFile image) {
        return beeDiagnosisService.diagnoseBeeDisease(image);
    }
}
