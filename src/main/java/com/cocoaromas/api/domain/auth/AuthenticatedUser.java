package com.cocoaromas.api.domain.auth;

public record AuthenticatedUser(Long id, String name, String email, Role role) {
}
