package kr.co.webee.presentation.interestpesticide.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.interestpesticide.dto.request.InterestPesticideRegisterRequest;
import kr.co.webee.presentation.interestpesticide.dto.response.InterestPesticideRegisterResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Interest Pesticide", description = "사용자 관심 농약 API")
public interface InterestPesticideApi {

    @Operation(
            summary = "관심 농약 등록",
            description = "농약 등록번호로 관심 농약을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterestPesticideRegisterResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.INTEREST_PESTICIDE_ALREADY_EXISTS)
    InterestPesticideRegisterResponse registerInterestPesticide(
            @RequestBody @Valid InterestPesticideRegisterRequest request,
            @Parameter(hidden = true) @UserId Long userId
    );
}
