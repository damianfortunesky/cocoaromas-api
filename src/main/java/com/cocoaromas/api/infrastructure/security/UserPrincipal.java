package com.cocoaromas.api.infrastructure.security;

import com.cocoaromas.api.domain.auth.Role;

public record UserPrincipal(Long userId, String email, Role role) {
}
