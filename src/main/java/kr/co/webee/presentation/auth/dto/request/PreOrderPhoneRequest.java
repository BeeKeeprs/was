package kr.co.webee.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(description = "사전예약용 전화번호 등록 request")
public record PreOrderPhoneRequest(
        @Pattern(
                regexp = "^010\\d{8}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        @Schema(description = "전화번호", example = "01012345678")
        @NotBlank
        String phoneNumber
) {
}
