package com.cocoaromas.api.entrypoints.rest.auth;

import com.cocoaromas.api.domain.auth.AuthToken;
import com.cocoaromas.api.domain.auth.AuthenticatedUser;
import com.cocoaromas.api.domain.auth.RegisterCommand;
import com.cocoaromas.api.domain.auth.RegisteredUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(
            @NotBlank(message = "email es requerido") @Email(message = "email inválido") String email,
            @NotBlank(message = "password es requerido") String password
    ) {
    }

    public record RegisterRequest(
            @NotBlank(message = "email es requerido") @Email(message = "email inválido") String email,
            @NotBlank(message = "password es requerido")
            @Size(min = 8, max = 72, message = "password debe tener entre 8 y 72 caracteres")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                    message = "password debe incluir letras y números"
            ) String password
    ) {
        public RegisterCommand toCommand() {
            return new RegisterCommand(email, password);
        }
    }

    public record RegisterResponse(
            @Schema(example = "12") Long id,
            @Schema(example = "ana.perez@example.com") String email,
            @Schema(example = "client") String role,
            OffsetDateTime createdAt
    ) {
        public static RegisterResponse fromDomain(RegisteredUser registeredUser) {
            return new RegisterResponse(
                    registeredUser.id(),
                    registeredUser.email(),
                    registeredUser.role().name().toLowerCase(),
                    registeredUser.createdAt()
            );
        }
    }

    public record AuthUserResponse(Long id, String email, String role) {
        public static AuthUserResponse fromDomain(AuthenticatedUser user) {
            return new AuthUserResponse(user.id(), user.email(), user.role().name().toLowerCase());
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
