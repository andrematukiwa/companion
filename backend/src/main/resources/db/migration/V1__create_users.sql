CREATE TABLE users (
    id         UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL,
    profile    VARCHAR(20)  NOT NULL DEFAULT 'DEFAULT',
    active     BOOLEAN      NOT NULL DEFAULT true,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_profile CHECK (profile IN ('DEVELOPER', 'STUDENT', 'DEFAULT'))
);
