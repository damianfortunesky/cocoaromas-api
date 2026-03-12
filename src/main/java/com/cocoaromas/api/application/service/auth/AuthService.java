package com.cocoaromas.api.application.service.auth;

import com.cocoaromas.api.application.port.in.auth.GetCurrentUserUseCase;
import com.cocoaromas.api.application.port.in.auth.LoginUseCase;
import com.cocoaromas.api.application.port.out.auth.LoadUserPort;
import com.cocoaromas.api.application.port.out.auth.PasswordHasherPort;
import com.cocoaromas.api.application.port.out.auth.SecurityContextPort;
import com.cocoaromas.api.application.port.out.auth.TokenProviderPort;
import com.cocoaromas.api.domain.auth.AuthToken;
import com.cocoaromas.api.domain.auth.AuthenticatedUser;
import com.cocoaromas.api.domain.auth.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements LoginUseCase, GetCurrentUserUseCase {

    private final LoadUserPort loadUserPort;
    private final PasswordHasherPort passwordHasherPort;
    private final TokenProviderPort tokenProviderPort;
    private final SecurityContextPort securityContextPort;

    public AuthService(
            LoadUserPort loadUserPort,
            PasswordHasherPort passwordHasherPort,
            TokenProviderPort tokenProviderPort,
            SecurityContextPort securityContextPort
    ) {
        this.loadUserPort = loadUserPort;
        this.passwordHasherPort = passwordHasherPort;
        this.tokenProviderPort = tokenProviderPort;
        this.securityContextPort = securityContextPort;
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
}
