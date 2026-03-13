package com.cocoaromas.api.domain.auth;

public record AuthenticatedUser(Long id, String email, Role role) {
}
