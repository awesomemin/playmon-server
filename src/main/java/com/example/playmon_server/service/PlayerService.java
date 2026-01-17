package com.example.playmon_server.service;

import com.example.playmon_server.domain.Player;
import com.example.playmon_server.dto.PlayerResponse;
import com.example.playmon_server.dto.riot.RiotDto;
import com.example.playmon_server.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final RestClient restClient;

    public PlayerResponse getPlayer(String gameName, String tagLine) {
        String puuid = getPuuid(gameName, tagLine);
        RiotDto.SummonerV4 summonerV4Dto = restClient.get()
                .uri("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid)
                .retrieve()
                .body(RiotDto.SummonerV4.class);
        assert summonerV4Dto != null;
        Player player = saverOrUpdate(puuid, gameName, tagLine, summonerV4Dto.profileIconId(), summonerV4Dto.summonerLevel(), summonerV4Dto.revisionDate());
        return new PlayerResponse(
                player.getGameName(),
                player.getTagLine(),
                player.getSummonerLevel(),
                player.getProfileIconId()
        );
    }

    // TODO - 나중에 getPuuid 말고 getAccount로 바꾸기 (대소문자 이슈)
    public String getPuuid(String gameName, String tagLine) {
        Optional<Player> optionalPlayer = playerRepository.findByGameNameTagLine(gameName, tagLine);
        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get().getPuuid();
        } else {
            RiotDto.AccountV1 accountV1Dto = restClient.get()
                    .uri("https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"+gameName+"/"+tagLine)
                    .retrieve()
                    .body(RiotDto.AccountV1.class);
            return accountV1Dto.puuid();
        }
    }

    private Player saverOrUpdate
    (
        String puuid,
        String gameName,
        String tagLine,
        int profileIconId,
        long summonerLevel,
        long revisionDate
    )
    {
        Player player = playerRepository.findByPuuid(puuid)
                .map(entity -> entity.update(gameName, tagLine, revisionDate, profileIconId, summonerLevel))
                .orElse(Player.builder()
                        .puuid(puuid)
                        .gameName(gameName)
                        .tagLine(tagLine)
                        .profileIconId(profileIconId)
                        .summonerLevel(summonerLevel)
                        .revisionDate(revisionDate)
                        .build());
        return playerRepository.save(player);
    }
}