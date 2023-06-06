-- Change Log: V1_0
-- Description: Create party_player_settings table
-- Created: 2023-06-03

CREATE TABLE party_player_settings
(
    player_uuid      binary(16) NOT NULL,
    allow_invitation BIT(1)     NOT NULL DEFAULT 1,
    CONSTRAINT PRIMARY KEY (player_uuid)
);