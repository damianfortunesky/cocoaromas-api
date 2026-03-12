package com.cocoaromas.api.domain.auth;

public record User(Long id, String name, String email, String username, String passwordHash, Role role) {
}
