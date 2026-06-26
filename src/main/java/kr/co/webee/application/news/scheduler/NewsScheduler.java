package kr.co.webee.application.news.scheduler;

import kr.co.webee.application.news.service.NewsCollectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsScheduler {

    private final NewsCollectService newsCollectService;

    @Scheduled(cron = "0 0 */6 * * *")
    public void collectNewsArticles() {
        newsCollectService.collectNewsArticles();
        log.info("뉴스 수집 스케줄링 완료. fetchedAt={}", LocalDateTime.now());
    }
}
