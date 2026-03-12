package com.cocoaromas.api.application.port.in.auth;

import com.cocoaromas.api.domain.auth.AuthenticatedUser;

public interface GetCurrentUserUseCase {
    AuthenticatedUser getCurrentUser();
}
