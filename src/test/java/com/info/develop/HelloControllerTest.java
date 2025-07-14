package com.info.develop;

import com.jayway.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:user-management-app;DB_CLOSE_ON_EXIT=FALSE"
})
public class HelloControllerTest {
    @Value("${local.server.port}")
    int port;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
    }

    @Test
    public void testWelcome() throws Exception {
        when().get("/api/").then()
                .statusCode(200)
                .body("message", is("Welcome to User Management API with Authentication"))
                .body("version", is("1.0.0"));
    }

    @Test
    public void testHealth() throws Exception {
        when().get("/api/health").then()
                .statusCode(200)
                .body("status", is("UP"))
                .body("service", is("User Management API"));
    }

    @Test
    public void testCalc() throws Exception {
        given().param("left", 100)
                .param("right", 200)
                .get("/api/calc")
                .then()
                .statusCode(200)
                .body("left", is(100))
                .body("right", is(200))
                .body("answer", is(300));
    }
}