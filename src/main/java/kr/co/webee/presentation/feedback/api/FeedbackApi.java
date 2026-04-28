package kr.co.webee.presentation.feedback.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.feedback.dto.FeedbackSubmitRequest;
import kr.co.webee.presentation.support.annotation.ApiDocsErrorType;
import kr.co.webee.presentation.support.annotation.UserId;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Feedback", description = "사용자 피드백 API")
public interface FeedbackApi {
    @Operation(
            summary = "피드백 제출",
            description = "로그인한 사용자의 서비스 피드백을 관리자 메일로 발송합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 전송 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
    })
    @ApiDocsErrorType({
            ErrorType.ENTITY_NOT_FOUND,
            ErrorType.UNHANDLED_EXCEPTION
    })
    String submitFeedback(
            @Parameter(
                    description = "피드백 내용",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackSubmitRequest.class)
                    )
            )
            @RequestBody @Valid FeedbackSubmitRequest request,
            @Parameter(hidden = true) @UserId Long userId
    );
}
