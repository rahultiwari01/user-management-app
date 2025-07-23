package com.info.develop;

import com.info.develop.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:user-management-app-test;DB_CLOSE_ON_EXIT=FALSE"
})
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/users";
    }

    @Test
    public void testGetAllUsers() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("John"));
        assertTrue(response.getBody().contains("jane.smith@example.com"));
    }

    @Test
    public void testGetUserById() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/1", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("John"));
        assertTrue(response.getBody().contains("Doe"));
    }

    @Test
    public void testGetUserByIdNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/999", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateUser() {
        User newUser = new User("Test", "User", "test.user@example.com", "+1-555-9999");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl, newUser, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User createdUser = response.getBody();
        assertNotNull(createdUser);
        assertEquals("Test", createdUser.getFirstName());
        assertEquals("test.user@example.com", createdUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User("Updated", "Name", "updated.email@example.com", "+1-555-8888");
        HttpEntity<User> requestEntity = new HttpEntity<>(updatedUser);

        ResponseEntity<User> response = restTemplate.exchange(
            baseUrl + "/2", HttpMethod.PUT, requestEntity, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals("Updated", responseUser.getFirstName());
    }

    @Test
    public void testDeleteUser() {
        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/3", HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify user is deleted
        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl + "/3", String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void testSearchUsersByEmail() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/search/email?q=alice", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("alice.williams@example.com"));
    }
}