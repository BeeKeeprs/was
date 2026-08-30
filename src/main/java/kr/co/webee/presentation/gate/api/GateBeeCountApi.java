package kr.co.webee.presentation.gate.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.webee.application.gate.dto.response.GateBeeCountResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.domain.hive.type.Period;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "개폐기 벌 카운트 API", description = "개폐기 벌 카운트 관련 API")
public interface GateBeeCountApi {

    @Operation(summary = "벌 카운트 조회", description = "개폐기의 벌 카운트 데이터를 기간별로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GateBeeCountResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.GATE_NOT_FOUND)
    GateBeeCountResponse getBeeCount(
            @Parameter(description = "개폐기 ID", example = "1", required = true)
            @PathVariable Long gateId,

            @Parameter(hidden = true) @UserId Long userId,

            @Parameter(description = "조회 기간", example = "DAY", required = true)
            @RequestParam Period period
    );
}
