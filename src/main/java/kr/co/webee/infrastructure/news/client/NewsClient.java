package kr.co.webee.infrastructure.news.client;

import kr.co.webee.infrastructure.news.dto.NaverNewsResponse;

public interface NewsClient {
    NaverNewsResponse fetchNews(String keyword);
}
