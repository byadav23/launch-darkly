package com.example.spring_boot_ld;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final String FEATURE_FLAG_KEY = "feature-one";
    private final LDClient ldClient;

    public Controller(LDClient ldClient) {
        this.ldClient = ldClient;
    }

    @GetMapping("/callMe")
    public String message(@RequestParam String userId) {

        final LDContext context = LDContext.builder("attempt-target-values-single-app")
                .name("Shared Organization Context")
                .set("userId", userId)
                .build();

        System.out.println(context.getKind() + " - This is kind");

        boolean flagValue = ldClient.boolVariation(FEATURE_FLAG_KEY, context, false);
        System.out.println("The '" + FEATURE_FLAG_KEY + "' feature flag evaluates to " + flagValue + "." + "for user : " + userId + " Context details are - " + context);

        return "Welcome to application ONE - <br>flag evaluated to - " + flagValue + ". <br>" + "for user : " + userId + " <br>Context details are - " + context;
    }
}
