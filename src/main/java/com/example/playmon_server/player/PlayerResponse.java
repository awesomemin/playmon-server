package com.example.playmon_server.player;

public record PlayerResponse(
        long id,
        String gameName,
        String tagLine,
        long summonerLevel,
        int profileIconId
) {
}