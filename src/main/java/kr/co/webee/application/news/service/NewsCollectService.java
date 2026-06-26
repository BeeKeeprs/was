package kr.co.webee.application.news.service;

import kr.co.webee.domain.interestnewskeyword.repository.InterestNewsKeywordRepository;
import kr.co.webee.infrastructure.news.client.NewsClient;
import kr.co.webee.infrastructure.news.dto.NaverNewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewsCollectService {
    private static final List<String> DEFAULT_KEYWORDS = List.of("수정벌", "꿀벌", "호박벌", "양봉");
    private final NewsClient newsClient;
    private final NewsService newsService;
    private final InterestNewsKeywordRepository interestNewsKeywordRepository;

    public void collectNewsArticles() {
        Set<String> keywords = new HashSet<>(DEFAULT_KEYWORDS);
        keywords.addAll(interestNewsKeywordRepository.findAllDistinctKeywords());

        for (String keyword : keywords) {
            NaverNewsResponse response = newsClient.fetchNews(keyword);
            newsService.addNewsArticleByKeyword(keyword, response.items());
        }
    }
}
