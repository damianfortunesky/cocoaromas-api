package com.cocoaromas.api.infrastructure.persistence.adapter;

import com.cocoaromas.api.application.port.out.auth.LoadUserPort;
import com.cocoaromas.api.application.port.out.auth.RegisterUserPort;
import com.cocoaromas.api.domain.auth.RegisteredUser;
import com.cocoaromas.api.domain.auth.User;
import com.cocoaromas.api.domain.auth.UserToRegister;
import com.cocoaromas.api.infrastructure.persistence.entity.UserEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.UserJpaRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements LoadUserPort, RegisterUserPort {

    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmailIgnoreCase(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public RegisteredUser save(UserToRegister userToRegister) {
        OffsetDateTime now = OffsetDateTime.now();
        UserEntity entity = new UserEntity();
        entity.setEmail(userToRegister.email());
        entity.setPasswordHash(userToRegister.passwordHash());
        entity.setRole(userToRegister.role());
        entity.setActive(true);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return toRegisteredUser(userJpaRepository.save(entity));
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.isActive()
        );
    }

    private RegisteredUser toRegisteredUser(UserEntity entity) {
        return new RegisteredUser(
                entity.getId(),
                entity.getEmail(),
                entity.getRole(),
                entity.getCreatedAt()
        );
    }
}
