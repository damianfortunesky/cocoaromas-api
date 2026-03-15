package com.cocoaromas.api.application.port.in.user;

import com.cocoaromas.api.domain.user.UpsertUserAddressCommand;
import com.cocoaromas.api.domain.user.UpsertUserProfileCommand;
import com.cocoaromas.api.domain.user.UserAddress;
import com.cocoaromas.api.domain.user.UserProfile;
import java.util.List;

public interface ManageMyProfileUseCase {
    UserProfile getProfile();
    UserProfile upsertProfile(UpsertUserProfileCommand command);
    List<UserAddress> listAddresses();
    UserAddress createAddress(UpsertUserAddressCommand command);
    UserAddress updateAddress(Long addressId, UpsertUserAddressCommand command);
    void deleteAddress(Long addressId);
}
