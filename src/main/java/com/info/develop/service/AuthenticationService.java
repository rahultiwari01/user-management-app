package com.info.develop.service;

import com.info.develop.dto.LoginRequest;
import com.info.develop.dto.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        String hostname = "unknown-host";
        try {
            // This will resolve to the pod's unique hostname (e.g., user-management-app-deployment-5f7d6b8c9c-abcde)
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Could not determine hostname", e);
        }
        log.info("[{}] - Attempting to authenticate user: {}", hostname, loginRequest.getUsername());
        try {
            // Authenticate using AuthenticationManager
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // If authentication is successful, generate a token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            final String jwtToken = jwtService.generateToken(userDetails);

            log.info("[{}] - User '{}' authenticated successfully. JWT token generated.", hostname, loginRequest.getUsername());
            return new LoginResponse("Login successful", loginRequest.getUsername(), true, jwtToken);
        } catch (BadCredentialsException e) {
            log.warn("[{}] - Authentication failed for user '{}': Invalid credentials", hostname, loginRequest.getUsername());
            return new LoginResponse("Invalid credentials", null, false, null);
        } catch (Exception e) {
            log.error("[{}] - An unexpected error occurred during authentication for user '{}'", hostname, loginRequest.getUsername(), e);
            return new LoginResponse("Authentication failed: " + e.getMessage(), null, false, null);
        }
    }

    public LoginResponse logout() {
        // In a stateless JWT implementation, logout is typically handled on the client-side
        // by simply deleting the token. The server doesn't need to do anything.
        return new LoginResponse("Logout successful. Please clear the token on the client side.", null, true, null);
    }

    // Note: The isAuthenticated and getCurrentUsername methods will no longer work as expected
    // across different requests in a stateless setup. The JWT filter will handle this.
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
            && !"anonymousUser".equals(authentication.getName());
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}