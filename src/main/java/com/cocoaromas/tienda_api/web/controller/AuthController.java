package com.cocoaromas.tienda_api.web.controller;

import com.cocoaromas.tienda_api.application.dto.request.LoginRequest;
import com.cocoaromas.tienda_api.application.dto.request.RegisterRequest;
import com.cocoaromas.tienda_api.application.dto.response.AuthResponse;
import com.cocoaromas.tienda_api.application.port.in.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

// Controlador de autenticación (register/login).
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authUseCase.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authUseCase.login(request);
    }
}