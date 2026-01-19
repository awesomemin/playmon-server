package com.example.playmon_server.notification;

import com.example.playmon_server.player.Player;
import com.example.playmon_server.subscription.SubscriptionRepository;
import com.example.playmon_server.user.User;
import com.example.playmon_server.user.UserRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public void notifySubscribers(Long playerId, Player player, long gameStartTime) {
        if (FirebaseApp.getApps().isEmpty()) {
            System.out.println("[FCM] Firebase not initialized. Skipping notification.");
            return;
        }

        List<Long> userIds = subscriptionRepository.findAllUserIdsByPlayerId(playerId);
        List<User> users = userRepository.findAllByIds(userIds);

        for (User user : users) {
            if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                sendNotification(user.getFcmToken(), player, gameStartTime);
            }
        }
    }

    private void sendNotification(String token, Player player, long gameStartTime) {
        String title = "New Game Started!";
        String body = player.getGameName() + "#" + player.getTagLine() + " started a new game";

        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("playerId", String.valueOf(player.getId()))
                .putData("gameStartTime", String.valueOf(gameStartTime))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("[FCM] Notification sent successfully: " + response);
        } catch (FirebaseMessagingException e) {
            System.out.println("[FCM Error] Failed to send notification: " + e.getMessage());
        }
    }
}
