package com.example.playmon_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${riot.api.key}")
    private String riotApiKey;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeader("X-Riot-Token", riotApiKey)
                .build();
    }
}