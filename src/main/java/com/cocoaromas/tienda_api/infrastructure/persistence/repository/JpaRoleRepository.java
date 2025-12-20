package com.cocoaromas.tienda_api.infrastructure.persistence.repository;

import com.cocoaromas.tienda_api.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByNameIgnoreCase(String name);
}
