package com.example.playmon_server.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription subscribePlayer(long userId, long playerId) {
        if(subscriptionRepository.exists(userId, playerId)) {
            throw new DuplicateSubscriptionException("이미 구독 중인 플레이어입니다.");
        }
        return subscriptionRepository.save(userId, playerId);
    }

    public void unsubscribe(long userId, long subscriptionId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if(optionalSubscription.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 구독 정보입니다.");
        }

        Subscription subscription = optionalSubscription.get();

        if(subscription.getUserId() != userId) {
            throw new IllegalStateException("본인의 구독만 취소할 수 있습니다.");
        }

        subscriptionRepository.deleteById(subscriptionId);
    }
}