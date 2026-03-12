package com.cocoaromas.api.application.port.out.auth;

import com.cocoaromas.api.domain.auth.User;

public interface TokenProviderPort {
    String generateAccessToken(User user);

    long getAccessTokenExpirationSeconds();
}
