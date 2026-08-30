CREATE TABLE gate
(
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    mac_address       VARCHAR(255) NOT NULL UNIQUE,
    name              VARCHAR(255) NOT NULL,
    region            VARCHAR(255) DEFAULT NULL,
    location          VARCHAR(255) DEFAULT NULL,
    memo              TEXT         DEFAULT NULL,
    is_connected      TINYINT(1)   NOT NULL DEFAULT 0,
    last_connected_at DATETIME(6)  DEFAULT NULL,
    user_id           BIGINT       NOT NULL,
    created_at        DATETIME(6)  NOT NULL,
    modified_at       DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gate_user FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE gate_telemetry
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    gate_id     BIGINT      NOT NULL,
    temperature DOUBLE      DEFAULT NULL,
    humidity    DOUBLE      DEFAULT NULL,
    recorded_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gate_telemetry_gate FOREIGN KEY (gate_id) REFERENCES gate (id)
);

CREATE TABLE gate_bee_count
(
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    gate_id      BIGINT      NOT NULL,
    entrance_in  INT         NOT NULL,
    entrance_out INT         NOT NULL,
    exit_in      INT         NOT NULL,
    exit_out     INT         NOT NULL,
    recorded_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gate_bee_count_gate FOREIGN KEY (gate_id) REFERENCES gate (id)
);
