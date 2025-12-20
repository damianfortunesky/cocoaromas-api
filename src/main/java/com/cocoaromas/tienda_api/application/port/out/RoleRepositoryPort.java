package com.cocoaromas.tienda_api.application.port.out;

import com.cocoaromas.tienda_api.domain.model.Role;
import java.util.Optional;

public interface RoleRepositoryPort {
    Optional<Role> findByName(String name);
    Optional<Integer> findRoleIdByName(String roleName);
}