package com.cocoaromas.api.entrypoints.rest.user;

import com.cocoaromas.api.domain.user.UpsertUserAddressCommand;
import com.cocoaromas.api.domain.user.UpsertUserProfileCommand;
import com.cocoaromas.api.domain.user.UserAddress;
import com.cocoaromas.api.domain.user.UserProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public final class UserProfileDtos {

    private UserProfileDtos() {}

    public record UpsertProfileRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            String phone,
            String dni,
            LocalDate birthDate
    ) {
        public UpsertUserProfileCommand toCommand() {
            return new UpsertUserProfileCommand(firstName, lastName, phone, dni, birthDate);
        }
    }

    public record ProfileResponse(
            Long userId,
            String email,
            String firstName,
            String lastName,
            String phone,
            String dni,
            LocalDate birthDate
    ) {
        public static ProfileResponse fromDomain(UserProfile profile) {
            return new ProfileResponse(profile.userId(), profile.email(), profile.firstName(), profile.lastName(), profile.phone(), profile.dni(), profile.birthDate());
        }
    }

    public record UpsertAddressRequest(
            @NotBlank String label,
            @NotBlank String receiverName,
            String receiverPhone,
            @NotBlank String street,
            String streetNumber,
            String floor,
            String apartment,
            @NotBlank String city,
            String state,
            @NotBlank String postalCode,
            @NotBlank @Size(min = 2, max = 2) String countryCode,
            String reference,
            Boolean defaultShipping,
            Boolean defaultBilling
    ) {
        public UpsertUserAddressCommand toCommand() {
            return new UpsertUserAddressCommand(label, receiverName, receiverPhone, street, streetNumber, floor, apartment, city, state, postalCode, countryCode, reference, defaultShipping, defaultBilling);
        }
    }

    public record AddressResponse(
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
        public static AddressResponse fromDomain(UserAddress address) {
            return new AddressResponse(address.id(), address.label(), address.receiverName(), address.receiverPhone(), address.street(), address.streetNumber(), address.floor(), address.apartment(), address.city(), address.state(), address.postalCode(), address.countryCode(), address.reference(), address.defaultShipping(), address.defaultBilling(), address.updatedAt());
        }
    }

    public record ErrorResponse(String code, String message) {}
}
