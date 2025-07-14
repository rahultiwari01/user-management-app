package com.info.develop.dto;

public class LoginResponse {
    private final String message;
    private final String username;
    private final boolean success;
    private final String token;

    public LoginResponse(String message, String username, boolean success, String token) {
        this.message = message;
        this.username = username;
        this.success = success;
        this.token = token;
    }

	public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }
}
