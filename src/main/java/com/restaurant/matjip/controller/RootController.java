package com.restaurant.matjip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/api/main";
    }

    @GetMapping("/health")
    public String health() {
        return "Start UP";
    }
}

