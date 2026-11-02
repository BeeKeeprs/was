ALTER TABLE gate
    ADD COLUMN hive_id BIGINT NULL,
    ADD CONSTRAINT fk_gate_hive FOREIGN KEY (hive_id) REFERENCES hive (id) ON DELETE SET NULL,
    ADD INDEX idx_gate_hive_id (hive_id);
