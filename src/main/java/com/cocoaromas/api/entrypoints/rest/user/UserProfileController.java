package com.cocoaromas.api.entrypoints.rest.user;

import com.cocoaromas.api.application.port.in.user.ManageMyProfileUseCase;
import com.cocoaromas.api.entrypoints.rest.user.UserProfileDtos.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/me")
public class UserProfileController {

    private final ManageMyProfileUseCase manageMyProfileUseCase;

    public UserProfileController(ManageMyProfileUseCase manageMyProfileUseCase) {
        this.manageMyProfileUseCase = manageMyProfileUseCase;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ProfileResponse getProfile() {
        return ProfileResponse.fromDomain(manageMyProfileUseCase.getProfile());
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ProfileResponse upsertProfile(@Valid @RequestBody UpsertProfileRequest request) {
        return ProfileResponse.fromDomain(manageMyProfileUseCase.upsertProfile(request.toCommand()));
    }

    @GetMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public List<AddressResponse> listAddresses() {
        return manageMyProfileUseCase.listAddresses().stream().map(AddressResponse::fromDomain).toList();
    }

    @PostMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse createAddress(@Valid @RequestBody UpsertAddressRequest request) {
        return AddressResponse.fromDomain(manageMyProfileUseCase.createAddress(request.toCommand()));
    }

    @PutMapping("/addresses/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public AddressResponse updateAddress(@PathVariable Long addressId, @Valid @RequestBody UpsertAddressRequest request) {
        return AddressResponse.fromDomain(manageMyProfileUseCase.updateAddress(addressId, request.toCommand()));
    }

    @DeleteMapping("/addresses/{addressId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long addressId) {
        manageMyProfileUseCase.deleteAddress(addressId);
    }
}
