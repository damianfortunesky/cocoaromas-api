package com.cocoaromas.tienda_api.infrastructure.persistence.adapter;

import com.cocoaromas.tienda_api.application.port.out.RoleRepositoryPort;
import com.cocoaromas.tienda_api.domain.model.Role;
import com.cocoaromas.tienda_api.infrastructure.persistence.repository.JpaRoleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final JpaRoleRepository jpaRoleRepository;

    public RoleRepositoryAdapter(JpaRoleRepository jpaRoleRepository) {
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRoleRepository.findByNameIgnoreCase(name)
                .map(r -> new Role(r.getRoleId(), r.getName()));
    }

    @Override
    public Optional<Integer> findRoleIdByName(String name) {
        return jpaRoleRepository.findByNameIgnoreCase(name)
                .map(r -> r.getRoleId());
    }
    
}