package kr.co.webee.presentation.news.controller;

import kr.co.webee.application.news.service.NewsService;
import kr.co.webee.presentation.news.api.NewsApi;
import kr.co.webee.presentation.news.dto.response.NewsArticleDetailResponse;
import kr.co.webee.presentation.news.dto.response.NewsArticleListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController implements NewsApi {

    private final NewsService newsService;

    @GetMapping
    public Slice<NewsArticleListResponse> getAllNewsArticleList(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return newsService.getAllNewsArticleList(keyword, PageRequest.of(page, size));
    }

    @GetMapping("/{newsArticleId}")
    public NewsArticleDetailResponse getNewsArticleDetail(@PathVariable Long newsArticleId) {
        return newsService.getNewsArticleDetail(newsArticleId);
    }
}
