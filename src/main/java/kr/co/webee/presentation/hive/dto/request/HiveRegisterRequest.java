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

        @Schema(description = "시리얼 넘버", example = "HV-20240315-0001")
        @NotBlank
        String serialNumber,

        @Schema(description = "메모", example = "월동 후 점검 필요")
        String memo
) {
    public Hive toEntity(User user) {
        return Hive.builder()
                .name(name)
                .region(region)
                .location(location)
                .serialNumber(serialNumber)
                .memo(memo)
                .user(user)
                .build();
    }
}
