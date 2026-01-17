package com.example.playmon_server.dto;

public record PlayerResponse(
        String gameName,
        String tagLine,
        long summonerLevel,
        int profileIconId
) {
}