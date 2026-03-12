package com.cocoaromas.api.application.port.in.auth;

import com.cocoaromas.api.domain.auth.AuthToken;

public interface LoginUseCase {
    AuthToken login(String identifier, String password);
}
