package com.project.moimtogether.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final Environment env;

    @GetMapping("/environment")
    public String environment() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> activeProfiles = Arrays.asList("prod1","prod2");
        String defaultProfile = profiles.isEmpty() ? "dev" : profiles.get(0);

        return profiles.stream()
                .filter(activeProfiles :: contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
