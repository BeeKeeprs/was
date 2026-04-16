package kr.co.webee.presentation.interestnewskeyword.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "뉴스 관심 키워드 등록 response")
public record InterestNewsKeywordRegisterResponse(
        @Schema(description = "뉴스 관심 키워드 등록 ID", example = "1")
        Long interestNewsKeywordId
) {
    public static InterestNewsKeywordRegisterResponse of(Long interestNewsKeywordId) {
        return InterestNewsKeywordRegisterResponse.builder()
                .interestNewsKeywordId(interestNewsKeywordId)
                .build();
    }
}

