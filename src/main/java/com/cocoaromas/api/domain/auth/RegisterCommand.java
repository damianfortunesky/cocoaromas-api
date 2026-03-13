package com.cocoaromas.api.domain.auth;

public record RegisterCommand(String firstName, String lastName, String email, String password) {
}
