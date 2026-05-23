package kr.co.webee.presentation.hive.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.hive.entity.Hive;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "벌통 등록 request")
public record HiveRegisterRequest(
        @Schema(description = "벌통 이름", example = "딸기 벌통")
        @NotBlank
        String name,

        @Schema(description = "지역", example = "충청북도 청주시 오창읍")
        @NotBlank
        String region,

        @Schema(description = "설치 위치", example = "과수원 남쪽")
        @NotBlank
        String location,

        @Schema(description = "벌통 MAC 주소", example = "AA:BB:CC:DD:EE:FF")
        @NotBlank
        String macAddress,

        @Schema(description = "메모", example = "월동 후 점검 필요")
        String memo
) {
    public Hive toEntity(User user) {
        return Hive.builder()
                .name(name)
                .region(region)
                .location(location)
                .macAddress(macAddress)
                .memo(memo)
                .user(user)
                .build();
    }
}
