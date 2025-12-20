package com.cocoaromas.tienda_api.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @Email
    @NotBlank
    public String email;

    @NotBlank
    @Size(min = 6, max = 100)
    public String password;

    @NotBlank
    @Size(max = 150)
    public String fullName;

    @Size(max = 50)
    public String phone;
}
