package com.example.playmon_server.controller;

import com.example.playmon_server.domain.Subscription;
import com.example.playmon_server.dto.CustomUserDetails;
import com.example.playmon_server.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @ResponseBody
    @PostMapping("/players/{playerId}/subscriptions")
    public Subscription subscribePlayer(@PathVariable long playerId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        long userId = userDetails.getUserId();
        return subscriptionService.subscribePlayer(userId, playerId);
    }
}