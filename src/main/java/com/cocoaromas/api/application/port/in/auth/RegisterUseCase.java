package com.cocoaromas.api.application.port.in.auth;

import com.cocoaromas.api.domain.auth.RegisterCommand;
import com.cocoaromas.api.domain.auth.RegisteredUser;

public interface RegisterUseCase {
    RegisteredUser register(RegisterCommand command);
}
