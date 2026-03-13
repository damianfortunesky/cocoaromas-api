package com.cocoaromas.api.domain.auth;

public record UserToRegister(
        String email,
        String passwordHash,
        Role role
) {
}
