package com.example.playmon_server.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {
    private final String name;
    private final long userId;

    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities, String name, long userId) {
        super(email, password, authorities);
        this.name = name;
        this.userId = userId;
    }
}