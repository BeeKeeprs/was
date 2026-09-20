ALTER TABLE hive_telemetry
    ADD COLUMN peltier_mode            VARCHAR(10)    NULL,
    ADD COLUMN peltier_duty_pct        DOUBLE         NULL,
    ADD COLUMN fan_hot_duty_pct        DOUBLE         NULL,
    ADD COLUMN fan_cold_duty_pct       DOUBLE         NULL,
    ADD COLUMN fan_state               VARCHAR(20)    NULL,
    ADD COLUMN target_temperature      DOUBLE         NULL,
    ADD COLUMN internal_sensor_valid   BOOLEAN        NULL,
    ADD COLUMN external_sensor_valid   BOOLEAN        NULL,
    ADD COLUMN peltier_cool_current_a  DOUBLE         NULL,
    ADD COLUMN peltier_heat_current_a  DOUBLE         NULL;
