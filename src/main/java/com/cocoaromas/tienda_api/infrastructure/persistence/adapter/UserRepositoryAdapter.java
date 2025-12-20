package com.cocoaromas.tienda_api.infrastructure.persistence.adapter;

import com.cocoaromas.tienda_api.application.dto.internal.CreatedUserDto;
import com.cocoaromas.tienda_api.application.dto.internal.UserAuthDto;
import com.cocoaromas.tienda_api.application.port.out.UserRepositoryPort;
import com.cocoaromas.tienda_api.infrastructure.persistence.entity.UserEntity;
import com.cocoaromas.tienda_api.infrastructure.persistence.repository.JpaRoleRepository;
import com.cocoaromas.tienda_api.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final JpaRoleRepository jpaRoleRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository,
                                 JpaRoleRepository jpaRoleRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional
    public CreatedUserDto createUser(String email, String passwordHash, String fullName, String phone) {
        UserEntity u = new UserEntity();
        u.setEmail(email);
        u.setPasswordHash(passwordHash);
        u.setFullName(fullName);
        u.setPhone(phone);

        UserEntity saved = jpaUserRepository.save(u);
        return new CreatedUserDto(saved.getUserId(), saved.getEmail());
    }

    @Override
    public Optional<UserAuthDto> findAuthByEmail(String email) {
        return jpaUserRepository.findByEmailIgnoreCase(email)
                .map(u -> new UserAuthDto(u.getUserId(), u.getEmail(), u.getFullName(), u.getPasswordHash(), u.isActive()));
    }

    @Override
    public List<String> findRoleNamesByUserId(Integer userId) {
        return jpaUserRepository.findById(userId)
                .map(u -> u.getRoles().stream().map(r -> r.getName().toUpperCase()).toList())
                .orElse(List.of());
    }

    @Override
    @Transactional
    public void addRoleToUser(Integer userId, Integer roleId) {
        var user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User no encontrado: " + userId));

        var role = jpaRoleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalStateException("Role no encontrado: " + roleId));

        user.getRoles().add(role);
        jpaUserRepository.save(user);
    }
}