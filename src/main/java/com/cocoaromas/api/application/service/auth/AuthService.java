package com.cocoaromas.api.application.service.auth;

import com.cocoaromas.api.application.port.in.auth.GetCurrentUserUseCase;
import com.cocoaromas.api.application.port.in.auth.LoginUseCase;
import com.cocoaromas.api.application.port.in.auth.RegisterUseCase;
import com.cocoaromas.api.application.port.out.auth.LoadUserPort;
import com.cocoaromas.api.application.port.out.auth.PasswordHasherPort;
import com.cocoaromas.api.application.port.out.auth.RegisterUserPort;
import com.cocoaromas.api.application.port.out.auth.SecurityContextPort;
import com.cocoaromas.api.application.port.out.auth.TokenProviderPort;
import com.cocoaromas.api.domain.auth.AuthToken;
import com.cocoaromas.api.domain.auth.AuthenticatedUser;
import com.cocoaromas.api.domain.auth.RegisterCommand;
import com.cocoaromas.api.domain.auth.RegisteredUser;
import com.cocoaromas.api.domain.auth.Role;
import com.cocoaromas.api.domain.auth.User;
import com.cocoaromas.api.domain.auth.UserToRegister;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements LoginUseCase, GetCurrentUserUseCase, RegisterUseCase {

    private final LoadUserPort loadUserPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenProviderPort tokenProviderPort;
    private final SecurityContextPort securityContextPort;
    private final RegisterUserPort registerUserPort;

    public AuthService(
            LoadUserPort loadUserPort,
            PasswordHasherPort passwordHasherPort,
            TokenProviderPort tokenProviderPort,
            SecurityContextPort securityContextPort,
            RegisterUserPort registerUserPort
    ) {
        this.loadUserPort = loadUserPort;
        this.passwordHasherPort = passwordHasherPort;
        this.tokenProviderPort = tokenProviderPort;
        this.securityContextPort = securityContextPort;
        this.registerUserPort = registerUserPort;
    }

    @Override
    public RegisteredUser register(RegisterCommand command) {
        validate(command);
        String normalizedEmail = command.email().trim().toLowerCase();
        if (registerUserPort.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException(normalizedEmail);
        }

        String passwordHash = passwordHasherPort.encode(command.password());
        String username = buildUsername(normalizedEmail);
        return registerUserPort.save(new UserToRegister(
                command.firstName().trim(),
                command.lastName().trim(),
                normalizedEmail,
                username,
                passwordHash,
                Role.CLIENT
        ));
    }

    @Override
    public AuthToken login(String identifier, String password) {
        User user = loadUserPort.findByEmailOrUsername(identifier)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasherPort.matches(password, user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = tokenProviderPort.generateAccessToken(user);
        return new AuthToken(
                accessToken,
                "Bearer",
                tokenProviderPort.getAccessTokenExpirationSeconds(),
                new AuthenticatedUser(user.id(), user.name(), user.email(), user.role())
        );
    }

    @Override
    public AuthenticatedUser getCurrentUser() {
        Long userId = securityContextPort.getAuthenticatedUserId();
        if (userId == null) {
            throw new UnauthorizedException("No hay usuario autenticado");
        }

        User user = loadUserPort.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Usuario autenticado no encontrado"));

        return new AuthenticatedUser(user.id(), user.name(), user.email(), user.role());
    }

    private void validate(RegisterCommand command) {
        if (command.firstName() == null || command.firstName().isBlank()) {
            throw new RegisterValidationException("El nombre es obligatorio");
        }
        if (command.lastName() == null || command.lastName().isBlank()) {
            throw new RegisterValidationException("El apellido es obligatorio");
        }
        if (command.password() == null || command.password().isBlank()) {
            throw new RegisterValidationException("La password es obligatoria");
        }
        if (command.password().length() < 8) {
            throw new RegisterValidationException("La password debe tener al menos 8 caracteres");
        }
    }

    private String buildUsername(String email) {
        String localPart = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        String normalized = localPart.trim().toLowerCase().replaceAll("[^a-z0-9._-]", "");
        if (normalized.isBlank()) {
            normalized = "client";
        }
        String suffix = Integer.toHexString(email.hashCode());
        String base = normalized.length() > 70 ? normalized.substring(0, 70) : normalized;
        return base + "_" + suffix;
    }
}
