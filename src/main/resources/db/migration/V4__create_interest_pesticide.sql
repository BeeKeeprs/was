CREATE TABLE interest_pesticide
(
    id                        BIGINT       NOT NULL AUTO_INCREMENT,
    user_id                   BIGINT       NOT NULL,
    pesticide_application_no  VARCHAR(20)  NOT NULL,
    brand_name                VARCHAR(100),
    product_name              VARCHAR(100),
    content_info              VARCHAR(50),
    safe_spray_interval       VARCHAR(50),
    crop_name                 VARCHAR(50),
    insect_name               VARCHAR(50),
    usage_name                VARCHAR(50),
    target_pest_name          VARCHAR(200),
    created_at                DATETIME     NOT NULL,
    modified_at               DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_interest_pesticide (user_id, pesticide_application_no),
    CONSTRAINT fk_interest_pesticide_user FOREIGN KEY (user_id) REFERENCES user (id)
);
