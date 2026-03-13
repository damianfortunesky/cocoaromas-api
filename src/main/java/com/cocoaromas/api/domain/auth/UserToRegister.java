package com.cocoaromas.api.domain.auth;

public record UserToRegister(
        String firstName,
        String lastName,
        String email,
        String username,
        String passwordHash,
        Role role
) {
}
