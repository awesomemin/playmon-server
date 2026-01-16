package com.example.playmon_server.domain;

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

    @Builder
    public User(Long id, String email, String name, String provider, String providerId, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    public User update(String name) {
        this.name = name;
        return this;
    }
}