package com.example.spring_boot_ld;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaunchDarklyServiceTest {

    @Mock
    private LDClient mockClient;

    private LaunchDarklyService launchDarklyService;

    @BeforeEach
    void setUp() {
        launchDarklyService = new LaunchDarklyService();
        ReflectionTestUtils.setField(launchDarklyService, "client", mockClient);
    }

    @Test
    void testCreateContext() {
        String userId = "testUser123";
        
        LDContext context = launchDarklyService.createContext(userId);
        
        assertNotNull(context);
        assertEquals("attempt-target-values-single-app", context.getKey());
        assertEquals("Shared Organization Context", context.getName());
        assertEquals(userId, context.getValue("userId").stringValue());
    }

    @Test
    void testCreateContextWithNullUserId() {
        LDContext context = launchDarklyService.createContext(null);
        
        assertNotNull(context);
        assertEquals("attempt-target-values-single-app", context.getKey());
        assertEquals("Shared Organization Context", context.getName());
    }

    @Test
    void testCreateContextWithEmptyUserId() {
        String userId = "";
        
        LDContext context = launchDarklyService.createContext(userId);
        
        assertNotNull(context);
        assertEquals("attempt-target-values-single-app", context.getKey());
        assertEquals("Shared Organization Context", context.getName());
        assertEquals(userId, context.getValue("userId").stringValue());
    }

    @Test
    void testCreateMultiContext() {
        String userId = "testUser123";
        
        LDContext multiContext = launchDarklyService.createMultiContext(userId);
        
        assertNotNull(multiContext);
        assertTrue(multiContext.isMultiple());
        assertEquals(2, multiContext.getIndividualContextCount());
    }

    @Test
    void testEvaluateFeatureFlagTrue() {
        LDContext context = launchDarklyService.createContext("testUser");
        when(mockClient.boolVariation(anyString(), any(LDContext.class), anyBoolean())).thenReturn(true);
        
        boolean result = launchDarklyService.evaluateFeatureFlag(context);
        
        assertTrue(result);
        verify(mockClient).boolVariation(LaunchDarklyService.FEATURE_FLAG_KEY, context, false);
    }

    @Test
    void testEvaluateFeatureFlagFalse() {
        LDContext context = launchDarklyService.createContext("testUser");
        when(mockClient.boolVariation(anyString(), any(LDContext.class), anyBoolean())).thenReturn(false);
        
        boolean result = launchDarklyService.evaluateFeatureFlag(context);
        
        assertFalse(result);
        verify(mockClient).boolVariation(LaunchDarklyService.FEATURE_FLAG_KEY, context, false);
    }

    @Test
    void testProcessFeatureFlagRequestWithTrueFlag() {
        String userId = "testUser123";
        String applicationName = "Test Application";
        when(mockClient.boolVariation(anyString(), any(LDContext.class), anyBoolean())).thenReturn(true);
        
        String result = launchDarklyService.processFeatureFlagRequest(userId, applicationName);
        
        assertNotNull(result);
        assertTrue(result.contains("Welcome to " + applicationName));
        assertTrue(result.contains("flag evaluated to - true"));
        assertTrue(result.contains("for user : " + userId));
        assertTrue(result.contains("Context details are"));
    }

    @Test
    void testProcessFeatureFlagRequestWithFalseFlag() {
        String userId = "testUser123";
        String applicationName = "Test Application";
        when(mockClient.boolVariation(anyString(), any(LDContext.class), anyBoolean())).thenReturn(false);
        
        String result = launchDarklyService.processFeatureFlagRequest(userId, applicationName);
        
        assertNotNull(result);
        assertTrue(result.contains("Welcome to " + applicationName));
        assertTrue(result.contains("flag evaluated to - false"));
        assertTrue(result.contains("for user : " + userId));
        assertTrue(result.contains("Context details are"));
    }

    @Test
    void testProcessFeatureFlagRequestWithSpecialCharacters() {
        String userId = "user@test.com";
        String applicationName = "Test & Application";
        when(mockClient.boolVariation(anyString(), any(LDContext.class), anyBoolean())).thenReturn(true);
        
        String result = launchDarklyService.processFeatureFlagRequest(userId, applicationName);
        
        assertNotNull(result);
        assertTrue(result.contains("Welcome to " + applicationName));
        assertTrue(result.contains("for user : " + userId));
    }

    @Test
    void testClose() throws Exception {
        doNothing().when(mockClient).close();
        
        assertDoesNotThrow(() -> launchDarklyService.close());
        
        verify(mockClient).close();
    }

    @Test
    void testCloseWithException() throws Exception {
        doThrow(new RuntimeException("Close error")).when(mockClient).close();
        
        assertDoesNotThrow(() -> launchDarklyService.close());
        
        verify(mockClient).close();
    }

    @Test
    void testConstants() {
        assertEquals("sdk-f291a2d3-6152-4b43-b4e5-2d7273f648be", LaunchDarklyService.SDK_KEY);
        assertEquals("feature-one", LaunchDarklyService.FEATURE_FLAG_KEY);
    }
}
