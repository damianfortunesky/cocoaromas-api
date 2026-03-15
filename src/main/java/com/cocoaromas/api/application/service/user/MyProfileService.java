package com.cocoaromas.api.application.service.user;

import com.cocoaromas.api.application.port.in.user.ManageMyProfileUseCase;
import com.cocoaromas.api.application.port.out.auth.SecurityContextPort;
import com.cocoaromas.api.application.service.auth.UnauthorizedException;
import com.cocoaromas.api.domain.user.UpsertUserAddressCommand;
import com.cocoaromas.api.domain.user.UpsertUserProfileCommand;
import com.cocoaromas.api.domain.user.UserAddress;
import com.cocoaromas.api.domain.user.UserProfile;
import com.cocoaromas.api.infrastructure.persistence.entity.UserEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.user.UserAddressEntity;
import com.cocoaromas.api.infrastructure.persistence.entity.user.UserDetailEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.UserJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.user.UserAddressJpaRepository;
import com.cocoaromas.api.infrastructure.persistence.repository.user.UserDetailJpaRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MyProfileService implements ManageMyProfileUseCase {

    private final SecurityContextPort securityContextPort;
    private final UserJpaRepository userJpaRepository;
    private final UserDetailJpaRepository userDetailJpaRepository;
    private final UserAddressJpaRepository userAddressJpaRepository;

    public MyProfileService(
            SecurityContextPort securityContextPort,
            UserJpaRepository userJpaRepository,
            UserDetailJpaRepository userDetailJpaRepository,
            UserAddressJpaRepository userAddressJpaRepository
    ) {
        this.securityContextPort = securityContextPort;
        this.userJpaRepository = userJpaRepository;
        this.userDetailJpaRepository = userDetailJpaRepository;
        this.userAddressJpaRepository = userAddressJpaRepository;
    }

    @Override
    public UserProfile getProfile() {
        UserEntity user = requireUser();
        UserDetailEntity detail = userDetailJpaRepository.findByUserId(user.getId()).orElse(null);
        return toProfile(user, detail);
    }

    @Override
    public UserProfile upsertProfile(UpsertUserProfileCommand command) {
        validateProfile(command);
        UserEntity user = requireUser();
        UserDetailEntity detail = userDetailJpaRepository.findByUserId(user.getId()).orElseGet(UserDetailEntity::new);
        OffsetDateTime now = OffsetDateTime.now();

        if (detail.getCreatedAt() == null) {
            detail.setCreatedAt(now);
            detail.setUser(user);
        }

        detail.setFirstName(normalize(command.firstName()));
        detail.setLastName(normalize(command.lastName()));
        detail.setPhone(normalize(command.phone()));
        detail.setDni(normalize(command.dni()));
        detail.setBirthDate(command.birthDate());
        detail.setUpdatedAt(now);

        UserDetailEntity saved = userDetailJpaRepository.save(detail);
        return toProfile(user, saved);
    }

    @Override
    public List<UserAddress> listAddresses() {
        Long userId = requireUserId();
        return userAddressJpaRepository.findByUserIdAndActiveTrueOrderByUpdatedAtDesc(userId).stream().map(this::toAddress).toList();
    }

    @Override
    public UserAddress createAddress(UpsertUserAddressCommand command) {
        validateAddress(command);
        UserEntity user = requireUser();
        UserAddressEntity entity = new UserAddressEntity();
        entity.setUser(user);
        applyAddress(command, entity, true);
        return toAddress(userAddressJpaRepository.save(entity));
    }

    @Override
    public UserAddress updateAddress(Long addressId, UpsertUserAddressCommand command) {
        validateAddress(command);
        Long userId = requireUserId();
        UserAddressEntity entity = userAddressJpaRepository.findByIdAndUserIdAndActiveTrue(addressId, userId)
                .orElseThrow(() -> new MyAddressNotFoundException(addressId));
        applyAddress(command, entity, false);
        return toAddress(userAddressJpaRepository.save(entity));
    }

    @Override
    public void deleteAddress(Long addressId) {
        Long userId = requireUserId();
        UserAddressEntity entity = userAddressJpaRepository.findByIdAndUserIdAndActiveTrue(addressId, userId)
                .orElseThrow(() -> new MyAddressNotFoundException(addressId));
        entity.setActive(false);
        entity.setUpdatedAt(OffsetDateTime.now());
        userAddressJpaRepository.save(entity);
    }

    private void applyAddress(UpsertUserAddressCommand command, UserAddressEntity entity, boolean create) {
        OffsetDateTime now = OffsetDateTime.now();
        entity.setLabel(command.label().trim());
        entity.setReceiverName(command.receiverName().trim());
        entity.setReceiverPhone(normalize(command.receiverPhone()));
        entity.setStreet(command.street().trim());
        entity.setStreetNumber(normalize(command.streetNumber()));
        entity.setFloor(normalize(command.floor()));
        entity.setApartment(normalize(command.apartment()));
        entity.setCity(command.city().trim());
        entity.setState(normalize(command.state()));
        entity.setPostalCode(command.postalCode().trim());
        entity.setCountryCode(command.countryCode().trim().toUpperCase());
        entity.setReference(normalize(command.reference()));
        entity.setDefaultShipping(Boolean.TRUE.equals(command.defaultShipping()));
        entity.setDefaultBilling(Boolean.TRUE.equals(command.defaultBilling()));
        entity.setActive(true);
        entity.setUpdatedAt(now);
        if (create) {
            entity.setCreatedAt(now);
        }

        if (entity.isDefaultShipping()) {
            clearDefaultShipping(entity.getUser().getId(), entity.getId());
        }
        if (entity.isDefaultBilling()) {
            clearDefaultBilling(entity.getUser().getId(), entity.getId());
        }
    }

    private void clearDefaultShipping(Long userId, Long currentId) {
        userAddressJpaRepository.findByUserIdAndActiveTrueOrderByUpdatedAtDesc(userId).stream()
                .filter(address -> currentId == null || !address.getId().equals(currentId))
                .filter(UserAddressEntity::isDefaultShipping)
                .forEach(address -> {
                    address.setDefaultShipping(false);
                    address.setUpdatedAt(OffsetDateTime.now());
                    userAddressJpaRepository.save(address);
                });
    }

    private void clearDefaultBilling(Long userId, Long currentId) {
        userAddressJpaRepository.findByUserIdAndActiveTrueOrderByUpdatedAtDesc(userId).stream()
                .filter(address -> currentId == null || !address.getId().equals(currentId))
                .filter(UserAddressEntity::isDefaultBilling)
                .forEach(address -> {
                    address.setDefaultBilling(false);
                    address.setUpdatedAt(OffsetDateTime.now());
                    userAddressJpaRepository.save(address);
                });
    }

    private void validateProfile(UpsertUserProfileCommand command) {
        if (command.firstName() == null || command.firstName().isBlank()) throw new MyProfileValidationException("firstName es requerido");
        if (command.lastName() == null || command.lastName().isBlank()) throw new MyProfileValidationException("lastName es requerido");
    }

    private void validateAddress(UpsertUserAddressCommand command) {
        if (command.label() == null || command.label().isBlank()) throw new MyProfileValidationException("label es requerido");
        if (command.receiverName() == null || command.receiverName().isBlank()) throw new MyProfileValidationException("receiverName es requerido");
        if (command.street() == null || command.street().isBlank()) throw new MyProfileValidationException("street es requerido");
        if (command.city() == null || command.city().isBlank()) throw new MyProfileValidationException("city es requerido");
        if (command.postalCode() == null || command.postalCode().isBlank()) throw new MyProfileValidationException("postalCode es requerido");
        if (command.countryCode() == null || command.countryCode().trim().length() != 2) throw new MyProfileValidationException("countryCode debe tener 2 caracteres");
    }

    private UserEntity requireUser() {
        Long userId = requireUserId();
        return userJpaRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));
    }

    private Long requireUserId() {
        Long userId = securityContextPort.getAuthenticatedUserId();
        if (userId == null) throw new UnauthorizedException("No autenticado");
        return userId;
    }

    private UserProfile toProfile(UserEntity user, UserDetailEntity detail) {
        return new UserProfile(
                user.getId(),
                user.getEmail(),
                detail == null ? null : detail.getFirstName(),
                detail == null ? null : detail.getLastName(),
                detail == null ? null : detail.getPhone(),
                detail == null ? null : detail.getDni(),
                detail == null ? null : detail.getBirthDate()
        );
    }

    private UserAddress toAddress(UserAddressEntity entity) {
        return new UserAddress(
                entity.getId(), entity.getLabel(), entity.getReceiverName(), entity.getReceiverPhone(), entity.getStreet(),
                entity.getStreetNumber(), entity.getFloor(), entity.getApartment(), entity.getCity(), entity.getState(),
                entity.getPostalCode(), entity.getCountryCode(), entity.getReference(), entity.isDefaultShipping(),
                entity.isDefaultBilling(), entity.getUpdatedAt()
        );
    }

    private String normalize(String raw) {
        return raw == null || raw.isBlank() ? null : raw.trim();
    }
}
