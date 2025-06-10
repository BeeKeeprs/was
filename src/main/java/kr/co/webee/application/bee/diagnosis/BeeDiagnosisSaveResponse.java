package kr.co.webee.application.bee.diagnosis;

public record BeeDiagnosisSaveResponse(
        Long beeDiagnosisId
) {
    public static BeeDiagnosisSaveResponse of(Long beeDiagnosisId) {
        return new BeeDiagnosisSaveResponse(beeDiagnosisId);
    }
}
