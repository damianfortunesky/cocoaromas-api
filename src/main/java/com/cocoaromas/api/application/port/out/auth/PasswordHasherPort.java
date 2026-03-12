package com.cocoaromas.api.application.port.out.auth;

public interface PasswordHasherPort {
    boolean matches(String rawPassword, String encodedPassword);
}
