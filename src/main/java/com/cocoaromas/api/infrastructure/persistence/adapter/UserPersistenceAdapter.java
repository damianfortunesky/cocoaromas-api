package com.cocoaromas.api.infrastructure.persistence.adapter;

import com.cocoaromas.api.application.port.out.auth.LoadUserPort;
import com.cocoaromas.api.domain.auth.User;
import com.cocoaromas.api.infrastructure.persistence.entity.UserEntity;
import com.cocoaromas.api.infrastructure.persistence.repository.UserJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements LoadUserPort {

    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByEmailOrUsername(String identifier) {
        return userJpaRepository.findByEmailIgnoreCaseOrUsernameIgnoreCase(identifier, identifier)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(this::toDomain);
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getPasswordHash(),
                entity.getRole()
        );
    }
}
