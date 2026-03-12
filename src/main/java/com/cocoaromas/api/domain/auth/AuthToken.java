package com.cocoaromas.api.domain.auth;

public record AuthToken(String accessToken, String tokenType, long expiresIn, AuthenticatedUser user) {
}
