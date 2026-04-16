package kr.co.webee.presentation.interestmarket.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.interestmarket.dto.request.InterestMarketRegisterRequest;
import kr.co.webee.presentation.interestmarket.dto.response.InterestMarketRegisterResponse;
import kr.co.webee.presentation.interestmarket.dto.response.InterestMarketResponse;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "InterestMarket", description = "사용자 관심 시장 API")
public interface InterestMarketApi {

    @Operation(
            summary = "관심 시장 등록",
            description = "시세 API 기준 시장 코드로 관심 시장을 등록합니다. (시장 코드만 / 시장 코드+작물 입력)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterestMarketRegisterResponse.class)
                    )
            ),
    })
    @ApiDocsErrorType(ErrorType.INTEREST_MARKET_ALREADY_EXISTS)
    InterestMarketRegisterResponse registerInterestMarket(
            @RequestBody @Valid InterestMarketRegisterRequest request,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "관심 시장 목록 조회", description = "등록한 관심 시장 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InterestMarketResponse.class))
                    )
            ),
    })
    List<InterestMarketResponse> getAllInterestMarkets(
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "관심 시장 삭제", description = "등록한 관심 시장을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
    })
    @ApiDocsErrorType(ErrorType.INTEREST_MARKET_NOT_FOUND)
    String removeInterestMarket(
            @Parameter(description = "관심 시장 등록 ID", example = "1", required = true)
            @PathVariable Long interestMarketId,
            @Parameter(hidden = true) @UserId Long userId
    );
}

