package com.example.playmon_server.service;

import com.example.playmon_server.domain.Subscription;
import com.example.playmon_server.exception.DuplicateSubscriptionException;
import com.example.playmon_server.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}