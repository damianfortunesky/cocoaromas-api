package com.cocoaromas.tienda_api.application.port.out;

import com.cocoaromas.tienda_api.application.dto.internal.CreatedUserDto;
import com.cocoaromas.tienda_api.application.dto.internal.UserAuthDto;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    boolean existsByEmail(String email);

    CreatedUserDto createUser(String email, String passwordHash, String fullName, String phone);

    Optional<UserAuthDto> findAuthByEmail(String email);

    List<String> findRoleNamesByUserId(Integer userId);

    void addRoleToUser(Integer userId, Integer roleId);
}