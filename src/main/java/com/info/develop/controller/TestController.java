package com.info.develop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/generate-passwords")
    public Map<String, String> generatePasswords() {
        Map<String, String> passwords = new HashMap<>();
        passwords.put("admin_password", passwordEncoder.encode("password"));
        passwords.put("user_user123", passwordEncoder.encode("user123"));
        passwords.put("others_password123", passwordEncoder.encode("password123"));
        return passwords;
    }
}