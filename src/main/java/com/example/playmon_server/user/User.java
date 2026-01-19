package com.example.playmon_server.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String name;
    private String provider;
    private String providerId;
    private Role role;
    private String fcmToken;

    @Builder
    public User(Long id, String email, String name, String provider, String providerId, Role role, String fcmToken) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.fcmToken = fcmToken;
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    public User updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }
}