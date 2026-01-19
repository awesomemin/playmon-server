package com.example.playmon_server.scheduler;

import com.example.playmon_server.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
public class PlayerQueueService {

    private final ConcurrentLinkedQueue<Long> playerQueue = new ConcurrentLinkedQueue<>();
    private final SubscriptionRepository subscriptionRepository;

    public void syncQueue() {
        List<Long> subscribedPlayerIds = subscriptionRepository.findAllSubscribedPlayerIds();
        Set<Long> currentSubscribed = new HashSet<>(subscribedPlayerIds);

        playerQueue.removeIf(id -> !currentSubscribed.contains(id));

        currentSubscribed.stream()
                .filter(id -> !playerQueue.contains(id))
                .forEach(playerQueue::offer);
    }

    public Long pollNextPlayer() {
        Long playerId = playerQueue.poll();
        if (playerId != null) {
            playerQueue.offer(playerId);
        }
        return playerId;
    }

    public int getQueueSize() {
        return playerQueue.size();
    }
}
