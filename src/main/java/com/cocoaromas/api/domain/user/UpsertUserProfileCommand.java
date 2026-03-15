package com.cocoaromas.api.domain.user;

import java.time.LocalDate;

public record UpsertUserProfileCommand(
        String firstName,
        String lastName,
        String phone,
        String dni,
        LocalDate birthDate
) {
}
