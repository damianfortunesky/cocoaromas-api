package com.cocoaromas.api.domain.user;

import java.time.LocalDate;

public record UserProfile(
        Long userId,
        String email,
        String firstName,
        String lastName,
        String phone,
        String dni,
        LocalDate birthDate
) {
}
