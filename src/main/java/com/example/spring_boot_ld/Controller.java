package com.example.spring_boot_ld;

import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.launchdarkly.sdk.*;
import com.launchdarkly.sdk.server.*;

@RestController
public class Controller {

    static String SDK_KEY = "sdk-f291a2d3-6152-4b43-b4e5-2d7273f648be";
    static String FEATURE_FLAG_KEY = "feature-one";

    @GetMapping("/callMe")
    public String message(@RequestParam String userId) {

        //code snippet
        LDConfig config = new LDConfig.Builder().build();
        final LDClient client = new LDClient(SDK_KEY, config);

        if (client.isInitialized()) {
            System.out.println("SDK successfully initialized!");
        } else {
            System.out.println("SDK failed to initialize.  Please check your internet connection and SDK credential for any typo.");
            System.exit(1);
        }

        final LDContext context = LDContext.builder("attempt-target-values-single-app")
                .name("Shared Organization Context")
                .set("userId", userId)
                .build();

        final LDContext multiContext = LDContext.createMulti(
                LDContext.builder("user-key-new-latest")
                        .set("userId", userId)
                        .build(),
                LDContext.builder("new-shared-user-context-latest")
                        .kind("organization")
                        .name("Shared Organization Context")
                        .set("userId", userId)
                        .build()
        );


        System.out.println(context.getKind() + " - This is kind");

        boolean flagValue = client.boolVariation(FEATURE_FLAG_KEY, context, false);
        System.out.println("The '" + FEATURE_FLAG_KEY + "' feature flag evaluates to " + flagValue + "." + "for user : " + userId + " Context details are - " + context);
        //try with different userId
        //code snippet
        return "Welcome to application ONE - <br>flag evaluated to - " + flagValue + ". <br>" + "for user : " + userId + " <br>Context details are - " + context;
    }
}
