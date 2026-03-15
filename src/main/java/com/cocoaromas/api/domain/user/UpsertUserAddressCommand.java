package com.cocoaromas.api.domain.user;

public record UpsertUserAddressCommand(
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
        Boolean defaultShipping,
        Boolean defaultBilling
) {
}
