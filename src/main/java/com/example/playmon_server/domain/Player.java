package com.example.playmon_server.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Player {
    private Long id;
    private String puuid;
    private String gameName;
    private String tagLine;
    private String lastPlayedGameId;
    private LocalDateTime revisionDate;
    private int profileIconId;
    private int summonerLevel;
}