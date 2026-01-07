package kr.co.webee.presentation.report.controller;

import kr.co.webee.application.report.service.HarvestPredictionService;
import kr.co.webee.presentation.report.api.HarvestPredictionApi;
import kr.co.webee.presentation.report.dto.HarvestPrediction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
@RestController
public class HarvestPredictionController implements HarvestPredictionApi {
    private final HarvestPredictionService harvestPredictionService;

    @Override
    @PostMapping("/harvest-prediction")
    public HarvestPrediction.Response getHarvestPredictionReports(@RequestBody HarvestPrediction.Request request) {
        return harvestPredictionService.getHarvestPredictionReports(request);
    }
}
