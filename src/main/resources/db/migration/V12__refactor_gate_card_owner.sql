ALTER TABLE gate_time_card
    DROP FOREIGN KEY fk_gate_time_card_gate,
    DROP COLUMN gate_id,
    ADD COLUMN user_id BIGINT NOT NULL AFTER id,
    ADD CONSTRAINT fk_gate_time_card_user FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE gate_count_card
    DROP FOREIGN KEY fk_gate_count_card_gate,
    DROP COLUMN gate_id,
    ADD COLUMN user_id BIGINT NOT NULL AFTER id,
    ADD CONSTRAINT fk_gate_count_card_user FOREIGN KEY (user_id) REFERENCES user (id);
