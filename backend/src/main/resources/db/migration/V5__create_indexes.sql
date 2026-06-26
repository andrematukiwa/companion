-- devices
CREATE INDEX idx_devices_user_id  ON devices (user_id);
CREATE INDEX idx_devices_active    ON devices (active);

-- device_tokens
CREATE INDEX idx_device_tokens_device_id ON device_tokens (device_id);
CREATE INDEX idx_device_tokens_token     ON device_tokens (token);

-- focus_sessions
CREATE INDEX idx_focus_sessions_user_id    ON focus_sessions (user_id);
CREATE INDEX idx_focus_sessions_started_at ON focus_sessions (started_at DESC);

-- context_snapshots
CREATE INDEX idx_context_snapshots_user_id     ON context_snapshots (user_id);
CREATE INDEX idx_context_snapshots_captured_at ON context_snapshots (captured_at DESC);

-- card_history
CREATE INDEX idx_card_history_device_id    ON card_history (device_id);
CREATE INDEX idx_card_history_displayed_at ON card_history (displayed_at DESC);
