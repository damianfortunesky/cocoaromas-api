package com.cocoaromas.api.entrypoints.rest.auth;

import com.cocoaromas.api.application.port.in.auth.GetCurrentUserUseCase;
import com.cocoaromas.api.application.port.in.auth.LoginUseCase;
import com.cocoaromas.api.application.port.in.auth.RegisterUseCase;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.AuthUserResponse;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.LoginRequest;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.LoginResponse;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.RegisterRequest;
import com.cocoaromas.api.entrypoints.rest.auth.AuthDtos.RegisterResponse;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Autenticación y usuario autenticado")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final RegisterUseCase registerUseCase;

    public AuthController(
            LoginUseCase loginUseCase,
            GetCurrentUserUseCase getCurrentUserUseCase,
            RegisterUseCase registerUseCase
    ) {
        this.loginUseCase = loginUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.registerUseCase = registerUseCase;
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

    @PostMapping("/register")
    @Operation(summary = "Registro público de usuario cliente", security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return RegisterResponse.fromDomain(registerUseCase.register(request.toCommand()));
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
