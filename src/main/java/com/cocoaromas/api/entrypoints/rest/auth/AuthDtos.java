package com.cocoaromas.api.entrypoints.rest.auth;

import com.cocoaromas.api.domain.auth.AuthToken;
import com.cocoaromas.api.domain.auth.AuthenticatedUser;
import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(
            @NotBlank(message = "emailOrUsername es requerido") String emailOrUsername,
            @NotBlank(message = "password es requerido") String password
    ) {
    }

    public record AuthUserResponse(Long id, String name, String email, String role) {
        public static AuthUserResponse fromDomain(AuthenticatedUser user) {
            return new AuthUserResponse(user.id(), user.name(), user.email(), user.role().name().toLowerCase());
        }
    }

    public record LoginResponse(String accessToken, String tokenType, long expiresIn, AuthUserResponse user) {
        public static LoginResponse fromDomain(AuthToken authToken) {
            return new LoginResponse(
                    authToken.accessToken(),
                    authToken.tokenType(),
                    authToken.expiresIn(),
                    AuthUserResponse.fromDomain(authToken.user())
            );
        }
    }

    public record ErrorResponse(String code, String message) {
    }
}
