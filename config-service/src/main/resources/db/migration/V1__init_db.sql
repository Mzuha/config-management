CREATE TABLE configs
(
    id           BIGSERIAL PRIMARY KEY,
    application  VARCHAR(255) NOT NULL,
    profile      VARCHAR(255) NOT NULL,
    config_key   VARCHAR(255) NOT NULL,
    config_value TEXT,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE
);

CREATE INDEX idx_config_app_profile ON configs (application, profile);
CREATE UNIQUE INDEX uk_config_app_profile_key ON configs (application, profile, config_key);