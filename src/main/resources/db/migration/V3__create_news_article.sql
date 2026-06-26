CREATE TABLE news_article
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    title         VARCHAR(300) NOT NULL,
    summary       VARCHAR(500),
    content       TEXT,
    source        VARCHAR(50),
    original_link VARCHAR(500) NOT NULL,
    published_at  DATETIME,
    fetched_at    DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_news_article_original_link (original_link)
);

CREATE TABLE news_article_keyword
(
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    news_article_id BIGINT      NOT NULL,
    keyword         VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_news_article_keyword (news_article_id, keyword),
    CONSTRAINT fk_news_article_keyword_article
        FOREIGN KEY (news_article_id) REFERENCES news_article (id)
);
