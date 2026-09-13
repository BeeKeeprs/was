ALTER TABLE gate_command
    ADD COLUMN operation          VARCHAR(255) NOT NULL DEFAULT 'EXECUTE' AFTER gate_id,
    ADD COLUMN target_command_id  VARCHAR(36)  DEFAULT NULL AFTER operation,
    ADD COLUMN title              VARCHAR(40)  DEFAULT NULL AFTER target_command_id,
    ADD COLUMN memo               VARCHAR(40)  DEFAULT NULL AFTER title,
    ADD COLUMN execution_status   VARCHAR(255) DEFAULT NULL AFTER detail,
    ADD COLUMN applied_at         DATETIME(6)  DEFAULT NULL AFTER execution_status;

ALTER TABLE gate_command
    MODIFY COLUMN card_type VARCHAR(255) DEFAULT NULL;
