CREATE TABLE hive_gate_action
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    hive_id        BIGINT       NOT NULL,
    title          VARCHAR(255) NOT NULL,
    action_type    VARCHAR(20)  NOT NULL,
    action_time    TIME         NOT NULL,
    repeat_enabled BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at     DATETIME(6)  NOT NULL,
    modified_at    DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_hive_gate_action_hive FOREIGN KEY (hive_id) REFERENCES hive (id)
);
