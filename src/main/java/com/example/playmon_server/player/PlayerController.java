package com.example.playmon_server.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/players/{gameName}/{tagLine}")
    @ResponseBody
    public PlayerResponse getPlayer(@PathVariable String gameName, @PathVariable String tagLine) {
        return playerService.getPlayer(gameName, tagLine);
    }
}