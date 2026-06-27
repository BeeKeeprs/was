package kr.co.webee.domain.post.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostCategory {
    KNOWHOW("노하우"),
    QUESTION("질문"),
    NEWS("소식"),
    MARKET("장터");

    private final String description;
}
