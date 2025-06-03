package kr.co.webee.infrastructure.bee.diagnosis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BeeDiseaseDetectResultResponse(
        boolean success,
        Prediction prediction,
        String error
) {
    public record Prediction(
            @JsonProperty("class") String type,
            double confidence
    ) {
    }
}

