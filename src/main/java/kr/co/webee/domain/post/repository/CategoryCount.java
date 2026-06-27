package kr.co.webee.domain.post.repository;

import kr.co.webee.domain.post.type.PostCategory;

public record CategoryCount(PostCategory category, long count) {
}
