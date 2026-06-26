package kr.co.webee.infrastructure.news.client;

import kr.co.webee.common.error.ErrorType;
import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.infrastructure.news.dto.NaverNewsResponse;
import kr.co.webee.infrastructure.news.properties.NewsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class NaverNewsClient implements NewsClient {
    private static final int DISPLAY = 100;

    private final RestClient restClient;
    private final NewsProperties newsProperties;

    public NaverNewsClient(NewsProperties newsProperties) {
        this.restClient = RestClient.create();
        this.newsProperties = newsProperties;
    }

    @Override
    public NaverNewsResponse fetchNews(String keyword) {
        URI uri = UriComponentsBuilder.fromHttpUrl(newsProperties.getApiUrl())
                .queryParam("query", keyword)
                .queryParam("display", DISPLAY)
                .queryParam("sort", "date")
                .build()
                .encode()
                .toUri();

        try {
            return restClient.get()
                    .uri(uri)
                    .header("X-Naver-Client-Id", newsProperties.getClientId())
                    .header("X-Naver-Client-Secret", newsProperties.getClientSecret())
                    .retrieve()
                    .body(NaverNewsResponse.class);
        } catch (Exception e) {
            log.error("네이버 뉴스 API 호출 실패. keyword={}, error={}", keyword, e.getMessage());
            throw new BusinessException(ErrorType.UNHANDLED_EXCEPTION);
        }
    }
}
