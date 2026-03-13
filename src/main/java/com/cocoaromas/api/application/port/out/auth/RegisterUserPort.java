package com.cocoaromas.api.application.port.out.auth;

import com.cocoaromas.api.domain.auth.RegisteredUser;
import com.cocoaromas.api.domain.auth.UserToRegister;

public interface RegisterUserPort {
    boolean existsByEmail(String email);

    RegisteredUser save(UserToRegister userToRegister);
}
