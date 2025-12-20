package com.cocoaromas.tienda_api.infrastructure.persistence.repository;

import com.cocoaromas.tienda_api.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
}