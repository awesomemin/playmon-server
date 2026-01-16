package com.example.playmon_server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Subscription {
    private Long id;
    private Long userId;
    private Long playerId;
}