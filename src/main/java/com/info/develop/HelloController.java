package com.info.develop;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    // Welcome endpoint
    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to User Management API with Authentication");
        response.put("version", "1.0.0");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("users", "/api/users");
        endpoints.put("health", "/api/health");
        endpoints.put("calculator", "/api/calc");
        endpoints.put("auth", "/api/auth");
        response.put("endpoints", endpoints);
        
        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put("loginEndpoint", "/api/auth/login");
        authInfo.put("logoutEndpoint", "/api/auth/logout");
        authInfo.put("statusEndpoint", "/api/auth/status");
        authInfo.put("sampleUsers", new String[]{"admin:password", "user:user123"});
        response.put("authentication", authInfo);
        
        return response;
    }

    // Health check endpoint
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "User Management API");
        return status;
    }

    @Data
    static class CalculationResult {
        private final int left;
        private final int right;
        private final long answer;
    }

    // Calculator endpoint (SQL sample)
    @GetMapping("/calc")
    public CalculationResult calculate(@RequestParam int left, @RequestParam int right) {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("left", left)
                .addValue("right", right);
        return jdbcTemplate.queryForObject("SELECT :left + :right AS answer", source,
                (rs, rowNum) -> new CalculationResult(left, right, rs.getLong("answer")));
    }
}
