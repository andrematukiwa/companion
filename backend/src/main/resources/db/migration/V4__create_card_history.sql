CREATE TABLE card_history (
    id           UUID      PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id    UUID      NOT NULL,
    card_type    VARCHAR(50)  NOT NULL,
    displayed_at TIMESTAMP NOT NULL DEFAULT now(),
    duration_ms  INTEGER   NOT NULL,

    CONSTRAINT fk_card_history_device FOREIGN KEY (device_id) REFERENCES devices (id)
);
