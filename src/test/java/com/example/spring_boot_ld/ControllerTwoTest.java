package com.example.spring_boot_ld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerTwo.class)
class ControllerTwoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LaunchDarklyService launchDarklyService;

    @Test
    void testCallMeTwoEndpoint() throws Exception {
        String userId = "testUser123";
        String expectedResponse = "Welcome to application TWO - <br>flag evaluated to - true. <br>for user : " + userId + " <br>Context details are - test context";
        
        when(launchDarklyService.processFeatureFlagRequest(userId, "application TWO"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/callMeTwo")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(launchDarklyService).processFeatureFlagRequest(userId, "application TWO");
    }

    @Test
    void testCallMeTwoEndpointWithDifferentUserId() throws Exception {
        String userId = "anotherUser456";
        String expectedResponse = "Welcome to application TWO - <br>flag evaluated to - false. <br>for user : " + userId + " <br>Context details are - test context";
        
        when(launchDarklyService.processFeatureFlagRequest(userId, "application TWO"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/callMeTwo")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(launchDarklyService).processFeatureFlagRequest(userId, "application TWO");
    }

    @Test
    void testCallMeTwoEndpointWithSpecialCharacters() throws Exception {
        String userId = "user@test.com";
        String expectedResponse = "Welcome to application TWO - <br>flag evaluated to - true. <br>for user : " + userId + " <br>Context details are - test context";
        
        when(launchDarklyService.processFeatureFlagRequest(userId, "application TWO"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/callMeTwo")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(launchDarklyService).processFeatureFlagRequest(userId, "application TWO");
    }

    @Test
    void testCallMeTwoEndpointWithEmptyUserId() throws Exception {
        String userId = "";
        String expectedResponse = "Welcome to application TWO - <br>flag evaluated to - false. <br>for user : " + userId + " <br>Context details are - test context";
        
        when(launchDarklyService.processFeatureFlagRequest(userId, "application TWO"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/callMeTwo")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(launchDarklyService).processFeatureFlagRequest(userId, "application TWO");
    }

    @Test
    void testCallMeTwoEndpointMissingUserIdParameter() throws Exception {
        mockMvc.perform(get("/callMeTwo"))
                .andExpect(status().isBadRequest());
    }
}
