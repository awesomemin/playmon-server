package com.example.playmon_server.dto.riot;

public class RiotDto {

    public record SummonerV4(
        int profileIconId,
        long revisionDate,
        String puuid,
        long summonerLevel
    ) {}

    public record AccountV1(
        String puuid,
        String gameName,
        String tagLine
    ) {}

    public record SpectatorV5(
        long gameId,
        long gameStartTime
    ) {}
}