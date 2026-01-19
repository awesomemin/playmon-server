package com.example.playmon_server.scheduler;

import com.example.playmon_server.dto.riot.RiotDto;
import com.example.playmon_server.notification.FcmService;
import com.example.playmon_server.player.Player;
import com.example.playmon_server.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GameCheckScheduler {

    private final PlayerQueueService playerQueueService;
    private final PlayerRepository playerRepository;
    private final RestClient restClient;
    private final FcmService fcmService;

    private final int ONE_MINUTE = 60000;
    private final int TWO_SECONDS = 2000;

    // DB 동기화
    @Scheduled(fixedRate = ONE_MINUTE)
    public void syncQueue() {
        playerQueueService.syncQueue();
        System.out.println("[Scheduler] Queue synced. Current size: " + playerQueueService.getQueueSize());
    }

    // 플레이어 게임 확인
    @Scheduled(fixedDelay = TWO_SECONDS)
    public void checkNextPlayer() {
        Long playerId = playerQueueService.pollNextPlayer();
        if (playerId == null) {
            return;
        }

        Optional<Player> playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isEmpty()) {
            return;
        }

        Player player = playerOpt.get();
        RiotDto.SpectatorV5 spectator = fetchCurrentGame(player.getPuuid());

        if (spectator != null) {
            String currentGameId = String.valueOf(spectator.gameId());
            if (!currentGameId.equals(player.getLastPlayedGameId())) {
                System.out.println("[New Game] " + player.getGameName() + "#" + player.getTagLine() + " started a new game! (gameId: " + currentGameId + ", gameStartTime: " + spectator.gameStartTime() + ")");
                playerRepository.updateLastPlayedGameId(playerId, currentGameId);
                fcmService.notifySubscribers(playerId, player, spectator.gameStartTime());
            }
        }
    }

    private RiotDto.SpectatorV5 fetchCurrentGame(String puuid) {
        try {
            return restClient.get()
                    .uri("https://kr.api.riotgames.com/lol/spectator/v5/active-games/by-summoner/" + puuid)
                    .retrieve()
                    .body(RiotDto.SpectatorV5.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            System.out.println("[Error] Failed to fetch game status for puuid: " + puuid + " - " + e.getMessage());
            return null;
        }
    }
}