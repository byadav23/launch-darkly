package com.example.spring_boot_ld;

import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaunchDarklyConfig {

    private static final String SDK_KEY = "sdk-f291a2d3-6152-4b43-b4e5-2d7273f648be";

    @Bean
    public LDClient ldClient() {
        LDConfig config = new LDConfig.Builder().build();
        LDClient client = new LDClient(SDK_KEY, config);
        
        if (client.isInitialized()) {
            System.out.println("SDK successfully initialized!");
        } else {
            System.out.println("SDK failed to initialize. Please check your internet connection and SDK credential for any typo.");
            throw new RuntimeException("LaunchDarkly SDK failed to initialize");
        }
        
        return client;
    }
}
