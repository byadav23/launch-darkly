package com.example.spring_boot_ld;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import org.springframework.stereotype.Service;

@Service
public class LaunchDarklyService {

    public static final String SDK_KEY = "sdk-f291a2d3-6152-4b43-b4e5-2d7273f648be";
    public static final String FEATURE_FLAG_KEY = "feature-one";

    private LDClient client;

    public LaunchDarklyService() {
        initializeClient();
    }

    private void initializeClient() {
        LDConfig config = new LDConfig.Builder().build();
        this.client = new LDClient(SDK_KEY, config);

        if (client.isInitialized()) {
            System.out.println("SDK successfully initialized!");
        } else {
            System.out.println("SDK failed to initialize. Please check your internet connection and SDK credential for any typo.");
            System.exit(1);
        }
    }

    public LDContext createContext(String userId) {
        return LDContext.builder("attempt-target-values-single-app")
                .name("Shared Organization Context")
                .set("userId", userId)
                .build();
    }

    public LDContext createMultiContext(String userId) {
        return LDContext.createMulti(
                LDContext.builder("user-key-new-latest")
                        .set("userId", userId)
                        .build(),
                LDContext.builder("new-shared-user-context-latest")
                        .kind("organization")
                        .name("Shared Organization Context")
                        .set("userId", userId)
                        .build()
        );
    }

    public boolean evaluateFeatureFlag(LDContext context) {
        return client.boolVariation(FEATURE_FLAG_KEY, context, false);
    }

    public String processFeatureFlagRequest(String userId, String applicationName) {
        LDContext context = createContext(userId);
        LDContext multiContext = createMultiContext(userId);

        System.out.println(context.getKind() + " - This is kind");

        boolean flagValue = evaluateFeatureFlag(context);
        System.out.println("The '" + FEATURE_FLAG_KEY + "' feature flag evaluates to " + flagValue + "." + "for user : " + userId + " Context details are - " + context);

        return "Welcome to " + applicationName + " - <br>flag evaluated to - " + flagValue + ". <br>" + "for user : " + userId + " <br>Context details are - " + context;
    }

    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                System.err.println("Error closing LaunchDarkly client: " + e.getMessage());
            }
        }
    }
}
