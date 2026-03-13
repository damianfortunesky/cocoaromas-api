package com.cocoaromas.api.domain.auth;

import java.time.OffsetDateTime;

public record RegisteredUser(
        Long id,
        String email,
        Role role,
        OffsetDateTime createdAt
) {
}
