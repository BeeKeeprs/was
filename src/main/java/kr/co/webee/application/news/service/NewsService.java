package kr.co.webee.application.news.service;

import kr.co.webee.common.error.exception.BusinessException;
import kr.co.webee.domain.news.entity.NewsArticle;
import kr.co.webee.domain.news.entity.NewsArticleKeyword;
import kr.co.webee.domain.news.repository.NewsArticleKeywordRepository;
import kr.co.webee.domain.news.repository.NewsArticleRepository;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.infrastructure.news.dto.NaverNewsResponse.Item;
import kr.co.webee.presentation.news.dto.response.NewsArticleDetailResponse;
import kr.co.webee.presentation.news.dto.response.NewsArticleListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsArticleRepository newsArticleRepository;
    private final NewsArticleKeywordRepository newsArticleKeywordRepository;

    @Transactional(readOnly = true)
    public Slice<NewsArticleListResponse> getAllNewsArticleList(String keyword, Pageable pageable) {
        return newsArticleRepository.findAllByKeyword(keyword, pageable)
                .map(NewsArticleListResponse::from);
    }

    @Transactional(readOnly = true)
    public NewsArticleDetailResponse getNewsArticleDetail(Long newsArticleId) {
        NewsArticle article = newsArticleRepository.findById(newsArticleId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENTITY_NOT_FOUND));

        return NewsArticleDetailResponse.from(article);
    }

    @Transactional
    public void addNewsArticleByKeyword(String keyword, List<Item> news) {
        for (Item item : news) {
            newsArticleRepository.findByOriginalLink(item.originalLink())
                    .ifPresentOrElse(
                            // 기존에 동일 뉴스가 존재하고 키워드 연결 안된 경우, 키워드만 연결 추가
                            article -> {
                                if (!newsArticleKeywordRepository.existsByNewsArticleIdAndKeyword(article.getId(), keyword)) {
                                    newsArticleKeywordRepository.save(NewsArticleKeyword.create(article, keyword));
                                }
                            },

                            // 새로운 뉴스일 경우 뉴스 및 키워드 저장
                            () -> {
                                NewsArticle article = newsArticleRepository.save(item.toEntity(LocalDateTime.now()));
                                newsArticleKeywordRepository.save(NewsArticleKeyword.create(article, keyword));
                            }
                    );
        }
    }
}
