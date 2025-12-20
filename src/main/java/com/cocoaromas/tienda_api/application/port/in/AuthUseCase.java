package com.cocoaromas.tienda_api.application.port.in;

import com.cocoaromas.tienda_api.application.dto.request.LoginRequest;
import com.cocoaromas.tienda_api.application.dto.request.RegisterRequest;
import com.cocoaromas.tienda_api.application.dto.response.AuthResponse;

public interface AuthUseCase {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}