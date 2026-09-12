package kr.co.webee.application.gate.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.co.webee.domain.gate.type.GateCardType;

@Schema(description = "개폐기 카드 실행 요청 (envelope)")
public record GateCommandRequest(
        @Schema(description = "개폐기 MAC 주소 (대문자, 콜론 구분)", example = "AA:BB:CC:DD:EE:FF")
        @NotBlank
        @Pattern(regexp = "^([0-9A-F]{2}:){5}[0-9A-F]{2}$", message = "MAC 주소 형식이 올바르지 않습니다")
        String gateId,

        @Schema(description = "카드 종류", example = "WINDOW")
        @NotNull
        GateCardType cardType,

        @Schema(description = "카드 제목 (앱 자동 생성)", example = "09:00~14:00 여닫기")
        @NotBlank
        @Size(max = 40)
        String title,

        @Schema(description = "메모 (선택)", example = "남쪽 과수원 아침 개방")
        @Size(max = 40)
        String memo,

        @Schema(description = "cardType별 payload — TIME / COUNT / 없음")
        JsonNode payload
) {
}
