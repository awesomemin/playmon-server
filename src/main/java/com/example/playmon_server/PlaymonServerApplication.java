package com.example.playmon_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PlaymonServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaymonServerApplication.class, args);
	}

}