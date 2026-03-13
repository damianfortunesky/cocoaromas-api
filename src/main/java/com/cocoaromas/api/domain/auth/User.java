package com.cocoaromas.api.domain.auth;

public record User(Long id, String email, String passwordHash, Role role, boolean active) {
}
