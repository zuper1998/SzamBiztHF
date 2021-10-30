package com.backend.backend.Communication.Response;

import java.util.UUID;

public class LoginResponse {

    private UUID id;

    private String username;

    private String email;

    private String jwtToken;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LoginResponse(UUID id, String username, String email, String jwtToken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.jwtToken = jwtToken;
    }
}
