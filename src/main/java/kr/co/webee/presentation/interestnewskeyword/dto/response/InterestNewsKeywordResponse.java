package kr.co.webee.presentation.interestnewskeyword.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.webee.domain.interestnewskeyword.entity.InterestNewsKeyword;
import lombok.Builder;

@Builder
@Schema(description = "뉴스 관심 키워드 정보")
public record InterestNewsKeywordResponse(
        @Schema(description = "뉴스 관심 키워드 등록 ID", example = "1")
        Long interestNewsKeywordId,

        @Schema(description = "관심 키워드", example = "토마토")
        String keyword
) {
    public static InterestNewsKeywordResponse from(InterestNewsKeyword entity) {
        return InterestNewsKeywordResponse.builder()
                .interestNewsKeywordId(entity.getId())
                .keyword(entity.getKeyword())
                .build();
    }
}

