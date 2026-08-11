DELETE FROM hive_control WHERE type IN ('FAN', 'HEATER', 'COOLER', 'CIRCULATION', 'CO2', 'DOOR');

ALTER TABLE hive_control
    MODIFY COLUMN type ENUM('TEMPERATURE', 'HUMIDITY') NOT NULL,
    DROP COLUMN auto_enabled,
    DROP COLUMN manual_enabled,
    DROP COLUMN is_on,
    ADD COLUMN target_value DOUBLE NULL;
