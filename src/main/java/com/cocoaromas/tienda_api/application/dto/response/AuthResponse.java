package com.cocoaromas.tienda_api.application.dto.response;

import java.time.Instant;
import java.util.List;

public class AuthResponse {
    public Integer userId;
    public String email;
    public String fullName;
    public List<String> roles;
    public String token;
    public Instant expiresAt;

    public AuthResponse(Integer userId, String email, String fullName, List<String> roles, String token, Instant expiresAt) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
        this.token = token;
        this.expiresAt = expiresAt;
    }
}