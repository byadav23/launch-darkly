package com.example.spring_boot_ld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTwo {

    private final LaunchDarklyService launchDarklyService;

    @Autowired
    public ControllerTwo(LaunchDarklyService launchDarklyService) {
        this.launchDarklyService = launchDarklyService;
    }

    @GetMapping("/callMeTwo")
    public String message(@RequestParam String userId) {
        return launchDarklyService.processFeatureFlagRequest(userId, "application TWO");
    }
}
