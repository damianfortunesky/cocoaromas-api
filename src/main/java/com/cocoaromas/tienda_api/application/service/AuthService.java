package com.cocoaromas.tienda_api.application.service;

import com.cocoaromas.tienda_api.application.dto.request.LoginRequest;
import com.cocoaromas.tienda_api.application.dto.request.RegisterRequest;
import com.cocoaromas.tienda_api.application.dto.response.AuthResponse;
import com.cocoaromas.tienda_api.application.port.in.AuthUseCase;
import com.cocoaromas.tienda_api.application.port.out.RoleRepositoryPort;
import com.cocoaromas.tienda_api.application.port.out.UserRepositoryPort;
import com.cocoaromas.tienda_api.common.exception.BusinessException;
import com.cocoaromas.tienda_api.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepo;
    private final RoleRepositoryPort roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepositoryPort userRepo,
                       RoleRepositoryPort roleRepo,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        String email = request.email.trim().toLowerCase();

        if (userRepo.existsByEmail(email)) {
            throw new BusinessException("El email ya está registrado");
        }

        Integer roleClient = roleRepo.findRoleIdByName("CLIENT")
                .orElseThrow(() -> new BusinessException("No existe el rol CLIENT en la tabla Roles"));

        String hash = passwordEncoder.encode(request.password);

        var created = userRepo.createUser(email, hash, request.fullName, request.phone);
        userRepo.addRoleToUser(created.userId, roleClient);

        List<String> roles = userRepo.findRoleNamesByUserId(created.userId);

        String token = jwtService.generateToken(created.email, roles);
        Instant expiresAt = jwtService.getLastExpiresAt();

        return new AuthResponse(created.userId, created.email, request.fullName, roles, token, expiresAt);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String email = request.email.trim().toLowerCase();

        var auth = userRepo.findAuthByEmail(email)
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!auth.isActive) {
            throw new BusinessException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(request.password, auth.passwordHash)) {
            throw new BusinessException("Credenciales inválidas");
        }

        List<String> roles = userRepo.findRoleNamesByUserId(auth.userId);

        String token = jwtService.generateToken(auth.email, roles);
        Instant expiresAt = jwtService.getLastExpiresAt();

        // Nota: fullName no lo traemos en login para mantenerlo simple.
        // Si querés, se agrega un método para traer fullName por email o userId.
        return new AuthResponse(auth.userId, auth.email, auth.fullName, roles, token, expiresAt);
    }
}