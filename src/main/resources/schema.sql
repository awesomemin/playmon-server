CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    provider VARCHAR(20) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    fcm_token VARCHAR(500),

    CONSTRAINT uk_oauth_id UNIQUE (provider, provider_id)
);

CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    puuid VARCHAR(100) NOT NULL UNIQUE,
    game_name VARCHAR(100) NOT NULL,
    tag_line VARCHAR(100) NOT NULL,
    last_played_game_id VARCHAR(50),
    revision_date BIGINT,
    profile_icon_id INT NOT NULL,
    summoner_level BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES players(id),
    CONSTRAINT uk_subscription_user_player UNIQUE (user_id, player_id)
);