package com.cocoaromas.api.application.port.out.auth;

public interface PasswordHasherPort {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
