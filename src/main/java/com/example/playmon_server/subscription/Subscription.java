package com.example.playmon_server.subscription;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Subscription {
    private Long id;
    private Long userId;
    private Long playerId;
}