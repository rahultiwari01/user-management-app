package com.info.develop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.develop.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:user-management-app-test;DB_CLOSE_ON_EXIT=FALSE"
})
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/users";
    }

    @Test
    public void testGetAllUsers() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().contains("John");
        assert response.getBody().contains("jane.smith@example.com");
    }

    @Test
    public void testGetUserById() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/1", String.class);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().contains("John");
        assert response.getBody().contains("Doe");
    }

    @Test
    public void testGetUserByIdNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/999", String.class);
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void testCreateUser() {
        User newUser = new User("Test", "User", "test.user@example.com", "+1-555-9999");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl, newUser, User.class);
        
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() != null;
        assert response.getBody().getFirstName().equals("Test");
        assert response.getBody().getEmail().equals("test.user@example.com");
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User("Updated", "Name", "updated.email@example.com", "+1-555-8888");
        HttpEntity<User> requestEntity = new HttpEntity<>(updatedUser);
        
        ResponseEntity<User> response = restTemplate.exchange(
            baseUrl + "/2", HttpMethod.PUT, requestEntity, User.class);
        
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getFirstName().equals("Updated");
    }

    @Test
    public void testDeleteUser() {
        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/3", HttpMethod.DELETE, null, Void.class);
        
        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
        
        // Verify user is deleted
        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl + "/3", String.class);
        assert getResponse.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void testSearchUsersByEmail() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/search/email?q=alice", String.class);
        
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().contains("alice.williams@example.com");
    }
}