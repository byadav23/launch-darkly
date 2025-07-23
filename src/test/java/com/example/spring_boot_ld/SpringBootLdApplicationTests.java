package com.example.spring_boot_ld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SpringBootLdApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertNotNull(restTemplate);
    }

    @Test
    void testCallMeEndpointIntegration() {
        String userId = "integrationTestUser";
        String url = "http://localhost:" + port + "/callMe?userId=" + userId;
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Welcome to application ONE"));
        assertTrue(response.getBody().contains("for user : " + userId));
        assertTrue(response.getBody().contains("flag evaluated to"));
    }

    @Test
    void testCallMeTwoEndpointIntegration() {
        String userId = "integrationTestUser2";
        String url = "http://localhost:" + port + "/callMeTwo?userId=" + userId;
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Welcome to application TWO"));
        assertTrue(response.getBody().contains("for user : " + userId));
        assertTrue(response.getBody().contains("flag evaluated to"));
    }

    @Test
    void testCallMeEndpointWithoutUserIdParameter() {
        String url = "http://localhost:" + port + "/callMe";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCallMeTwoEndpointWithoutUserIdParameter() {
        String url = "http://localhost:" + port + "/callMeTwo";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testBothEndpointsWithSameUserId() {
        String userId = "sharedUser123";
        
        String callMeUrl = "http://localhost:" + port + "/callMe?userId=" + userId;
        ResponseEntity<String> callMeResponse = restTemplate.getForEntity(callMeUrl, String.class);
        
        String callMeTwoUrl = "http://localhost:" + port + "/callMeTwo?userId=" + userId;
        ResponseEntity<String> callMeTwoResponse = restTemplate.getForEntity(callMeTwoUrl, String.class);
        
        assertEquals(HttpStatus.OK, callMeResponse.getStatusCode());
        assertEquals(HttpStatus.OK, callMeTwoResponse.getStatusCode());
        
        assertNotNull(callMeResponse.getBody());
        assertNotNull(callMeTwoResponse.getBody());
        
        assertTrue(callMeResponse.getBody().contains("application ONE"));
        assertTrue(callMeTwoResponse.getBody().contains("application TWO"));
        
        assertTrue(callMeResponse.getBody().contains("for user : " + userId));
        assertTrue(callMeTwoResponse.getBody().contains("for user : " + userId));
    }

    @Test
    void testEndpointsWithSpecialCharacterUserId() {
        String userId = "user@test.com";
        
        String callMeUrl = "http://localhost:" + port + "/callMe?userId=" + userId;
        ResponseEntity<String> response = restTemplate.getForEntity(callMeUrl, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("for user : " + userId));
    }
}
