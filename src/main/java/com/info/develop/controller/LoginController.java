package com.info.develop.controller;

import com.info.develop.dto.LoginRequest;
import com.info.develop.dto.LoginResponse;
import com.info.develop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // The AuthenticationService already handles the logic and potential exceptions.
        // This simplifies the controller's responsibility.
        LoginResponse response = authenticationService.authenticate(loginRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout() {
        try {
            LoginResponse response = authenticationService.logout();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse("Logout failed: " + e.getMessage(), null, false, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus() {
        Map<String, Object> status = new HashMap<>();
        boolean isAuthenticated = authenticationService.isAuthenticated();
        
        status.put("authenticated", isAuthenticated);
        status.put("username", isAuthenticated ? authenticationService.getCurrentUsername() : null);
        
        return ResponseEntity.ok(status);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getAuthInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("loginEndpoint", "/api/auth/login");
        info.put("logoutEndpoint", "/api/auth/logout");
        info.put("statusEndpoint", "/api/auth/status");
        info.put("method", "POST for login/logout, GET for status");
        info.put("sampleUsers", new String[]{"admin:password", "user:user123"});
        
        return ResponseEntity.ok(info);
    }
}