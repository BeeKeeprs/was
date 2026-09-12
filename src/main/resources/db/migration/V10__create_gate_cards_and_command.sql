DROP TABLE IF EXISTS hive_gate_action;

CREATE TABLE gate_time_card
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    gate_id        BIGINT       NOT NULL,
    action_type    VARCHAR(255) NOT NULL,
    start_hour     INT          DEFAULT NULL,
    end_hour       INT          DEFAULT NULL,
    repeat_enabled TINYINT(1)   NOT NULL DEFAULT 0,
    memo           TEXT         DEFAULT NULL,
    created_at     DATETIME(6)  NOT NULL,
    modified_at    DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gate_time_card_gate FOREIGN KEY (gate_id) REFERENCES gate (id)
);

CREATE TABLE gate_count_card
(
    id                   BIGINT     NOT NULL AUTO_INCREMENT,
    gate_id              BIGINT     NOT NULL,
    repeat_days          INT        NOT NULL,
    min_count            INT        NOT NULL,
    max_count            INT        NOT NULL,
    start_hour           INT        DEFAULT NULL,
    end_hour             INT        DEFAULT NULL,
    within_entrance_open TINYINT(1) NOT NULL DEFAULT 0,
    within_exit_open     TINYINT(1) NOT NULL DEFAULT 0,
    above_entrance_open  TINYINT(1) NOT NULL DEFAULT 0,
    above_exit_open      TINYINT(1) NOT NULL DEFAULT 0,
    memo                 TEXT       DEFAULT NULL,
    created_at           DATETIME(6) NOT NULL,
    modified_at          DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gate_count_card_gate FOREIGN KEY (gate_id) REFERENCES gate (id)
);

CREATE TABLE gate_command
(
    id           VARCHAR(36)  NOT NULL,
    gate_id      BIGINT       NOT NULL,
    card_type    VARCHAR(255) NOT NULL,
    payload_json TEXT         DEFAULT NULL,
    status       VARCHAR(255) NOT NULL,
    detail       VARCHAR(255) DEFAULT NULL,
    created_at   DATETIME(6)  NOT NULL,
    modified_at  DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gate_command_gate FOREIGN KEY (gate_id) REFERENCES gate (id)
);
