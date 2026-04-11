package kr.co.webee.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 등록 request")
public record UserInfoRegisterRequest(
        @Schema(description = "이름", example = "exampleName")
        @NotBlank
        String name
) {
}
