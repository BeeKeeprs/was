package kr.co.webee.presentation.inquiry.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.inquiry.dto.request.InquirySubmitRequest;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "문의 API", description = "문의 관련 API")
public interface InquiryApi {

    @Operation(
            summary = "문의 등록",
            description = "로그인 없이 문의를 제출하고 관리자 메일로 발송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문의 전송 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
    })
    @ApiDocsErrorType(ErrorType.UNHANDLED_EXCEPTION)
    String submitInquiry(
            @Parameter(
                    description = "문의 등록 요청 JSON",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InquirySubmitRequest.class)
                    )
            )
            @RequestBody @Valid InquirySubmitRequest request
    );
}
