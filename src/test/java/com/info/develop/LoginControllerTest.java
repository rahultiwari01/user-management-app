package com.info.develop;

import com.info.develop.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/auth";
    }

    private ResponseEntity<Map> performLoginRequest(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        return restTemplate.postForEntity(getBaseUrl() + "/login", entity, Map.class);
    }

    @Test
    public void testGetAuthInfo() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            getBaseUrl() + "/info", Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> info = response.getBody();
        assertNotNull(info);
        assertEquals("/api/auth/login", info.get("loginEndpoint"));
        assertEquals("/api/auth/logout", info.get("logoutEndpoint"));
        assertEquals("/api/auth/status", info.get("statusEndpoint"));
    }

    @Test
    public void testSuccessfulLogin() {
        ResponseEntity<Map> response = performLoginRequest("admin", "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(true, responseBody.get("success"));
        assertEquals("admin", responseBody.get("username"));
        assertEquals("Login successful", responseBody.get("message"));
    }

    @Test
    public void testFailedLogin() {
        ResponseEntity<Map> response = performLoginRequest("admin", "wrongpassword");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(false, responseBody.get("success"));
        assertEquals("Invalid credentials", responseBody.get("message"));
    }

    @Test
    public void testLoginWithNonExistentUser() {
        ResponseEntity<Map> response = performLoginRequest("nonexistent", "password");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(false, responseBody.get("success"));
        assertEquals("Invalid credentials", responseBody.get("message"));
    }

    @Test
    public void testLogout() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            getBaseUrl() + "/logout", null, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(true, responseBody.get("success"));
        assertEquals("Logout successful", responseBody.get("message"));
    }

    @Test
    public void testAuthStatus() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            getBaseUrl() + "/status", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> status = response.getBody();
        assertNotNull(status);
        assertTrue(status.containsKey("authenticated"));
        assertTrue(status.containsKey("username"));
    }

    @Test
    public void testSecondUserLogin() {
        ResponseEntity<Map> response = performLoginRequest("user", "user123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(true, responseBody.get("success"));
        assertEquals("user", responseBody.get("username"));
        assertEquals("Login successful", responseBody.get("message"));
    }
}