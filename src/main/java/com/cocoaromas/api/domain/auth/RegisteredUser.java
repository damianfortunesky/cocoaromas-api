package com.cocoaromas.api.domain.auth;

import java.time.OffsetDateTime;

public record RegisteredUser(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        OffsetDateTime createdAt
) {
}
