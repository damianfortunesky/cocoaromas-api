package com.cocoaromas.api.application.port.out.auth;

import com.cocoaromas.api.domain.auth.User;
import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findByEmailOrUsername(String identifier);

    Optional<User> findById(Long userId);
}
