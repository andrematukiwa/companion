CREATE TABLE focus_sessions (
    id         UUID      PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID      NOT NULL,
    started_at TIMESTAMP NOT NULL DEFAULT now(),
    ended_at   TIMESTAMP NULL,
    project    VARCHAR(150) NULL,
    pomodoros  INTEGER   NOT NULL DEFAULT 0,

    CONSTRAINT fk_focus_sessions_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE context_snapshots (
    id             UUID      PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id        UUID      NOT NULL,
    captured_at    TIMESTAMP NOT NULL DEFAULT now(),
    active_app     VARCHAR(100) NULL,
    active_branch  VARCHAR(150) NULL,
    active_project VARCHAR(150) NULL,
    payload        JSONB     NOT NULL DEFAULT '{}',

    CONSTRAINT fk_context_snapshots_user FOREIGN KEY (user_id) REFERENCES users (id)
);
