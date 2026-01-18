package com.example.playmon_server;

import com.example.playmon_server.user.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExampleController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "this is home.";
    }

    @GetMapping("/secret")
    @ResponseBody
    public String secret(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return "this is secret page. your information is / " + userDetails.getUsername() + " / " + userDetails.getName() + " / " + userDetails.getUserId();
    }
}