package kr.co.webee.presentation.interestnewskeyword.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.webee.domain.interestnewskeyword.entity.InterestNewsKeyword;
import kr.co.webee.domain.user.entity.User;
import lombok.Builder;

@Builder
@Schema(description = "뉴스 관심 키워드 등록 request")
public record InterestNewsKeywordRegisterRequest(
        @Schema(description = "관심 키워드", example = "토마토")
        @NotBlank
        String keyword
) {
    public InterestNewsKeyword toEntity(User user) {
        return InterestNewsKeyword.builder()
                .user(user)
                .keyword(keyword)
                .build();
    }
}

