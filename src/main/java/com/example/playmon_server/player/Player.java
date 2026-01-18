package com.example.playmon_server.player;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class Player {
    private Long id;
    private String puuid;
    private String gameName;
    private String tagLine;
    private String lastPlayedGameId;
    private Long revisionDate;
    private int profileIconId;
    private long summonerLevel;

    public Player update(String gameName, String tagLine, Long revisionDate, int profileIconId, long summonerLevel) {
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.revisionDate = revisionDate;
        this.profileIconId = profileIconId;
        this.summonerLevel = summonerLevel;
        return this;
    }
}