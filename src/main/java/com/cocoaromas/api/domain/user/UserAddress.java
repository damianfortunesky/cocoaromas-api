package com.cocoaromas.api.domain.user;

import java.time.OffsetDateTime;

public record UserAddress(
        Long id,
        String label,
        String receiverName,
        String receiverPhone,
        String street,
        String streetNumber,
        String floor,
        String apartment,
        String city,
        String state,
        String postalCode,
        String countryCode,
        String reference,
        boolean defaultShipping,
        boolean defaultBilling,
        OffsetDateTime updatedAt
) {
}
