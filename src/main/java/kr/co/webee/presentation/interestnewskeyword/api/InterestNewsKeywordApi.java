package kr.co.webee.presentation.interestnewskeyword.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.presentation.interestnewskeyword.dto.request.InterestNewsKeywordRegisterRequest;
import kr.co.webee.presentation.interestnewskeyword.dto.response.InterestNewsKeywordRegisterResponse;
import kr.co.webee.presentation.interestnewskeyword.dto.response.InterestNewsKeywordResponse;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Interest News Keyword", description = "사용자 뉴스 관심 키워드 API")
public interface InterestNewsKeywordApi {

    @Operation(summary = "뉴스 관심 키워드 등록", description = "사용자 뉴스 관심 키워드를 등록합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterestNewsKeywordRegisterResponse.class)
                    )
            ),
    })
    InterestNewsKeywordRegisterResponse registerInterestNewsKeyword(
            @RequestBody @Valid InterestNewsKeywordRegisterRequest request,
            @Parameter(hidden = true) @UserId Long userId
    );

    @Operation(summary = "뉴스 관심 키워드 조회", description = "사용자가 등록한 뉴스 관심 키워드를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InterestNewsKeywordResponse.class))
                    )
            ),
    })
    List<InterestNewsKeywordResponse> getInterestNewsKeywords(
            @Parameter(hidden = true) @UserId Long userId
    );
}

