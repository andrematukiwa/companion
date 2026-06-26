CREATE TABLE devices (
    id         UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID         NOT NULL,
    name       VARCHAR(100) NOT NULL,
    type       VARCHAR(20)  NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT true,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT fk_devices_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_devices_type CHECK (type IN ('KINDLE', 'TABLET', 'MOBILE'))
);

CREATE TABLE device_tokens (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id  UUID        NOT NULL,
    token      VARCHAR(64)  NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT false,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT uq_device_tokens_token UNIQUE (token),
    CONSTRAINT fk_device_tokens_device FOREIGN KEY (device_id) REFERENCES devices (id)
);
