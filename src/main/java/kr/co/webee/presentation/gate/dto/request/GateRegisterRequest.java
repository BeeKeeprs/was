package kr.co.webee.presentation.gate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.gate.entity.Gate;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "개폐기 등록 request")
public record GateRegisterRequest(
        @Schema(description = "개폐기 이름", example = "딸기 온실 개폐기")
        @NotBlank
        String name,

        @Schema(description = "지역", example = "충청북도 청주시 오창읍")
        String region,

        @Schema(description = "설치 위치", example = "온실 남쪽 출입구")
        String location,

        @Schema(description = "개폐기 MAC 주소", example = "AA:BB:CC:DD:EE:FF")
        @NotBlank
        String macAddress,

        @Schema(description = "메모", example = "점검 필요")
        String memo
) {
    public Gate toEntity(User user) {
        return Gate.builder()
                .name(name)
                .region(region)
                .location(location)
                .macAddress(macAddress)
                .memo(memo)
                .user(user)
                .build();
    }
}
