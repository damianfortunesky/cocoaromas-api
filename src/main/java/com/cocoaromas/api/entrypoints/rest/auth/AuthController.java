package com.cocoaromas.api.entrypoints.rest.auth;

import com.cocoaromas.api.application.port.in.auth.GetCurrentUserUseCase;
import com.cocoaromas.api.application.port.in.auth.LoginUseCase;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.AuthUserResponse;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.LoginRequest;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Autenticación y usuario autenticado")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public AuthController(LoginUseCase loginUseCase, GetCurrentUserUseCase getCurrentUserUseCase) {
        this.loginUseCase = loginUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Login por email o username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return LoginResponse.fromDomain(loginUseCase.login(request.emailOrUsername(), request.password()));
    }

    @GetMapping("/me")
    @Operation(summary = "Retorna el usuario autenticado actual", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario autenticado"),
            @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    public AuthUserResponse me() {
        return AuthUserResponse.fromDomain(getCurrentUserUseCase.getCurrentUser());
    }
}
