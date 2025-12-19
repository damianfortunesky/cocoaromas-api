package com.cocoaromas.tienda_api.application.dto.internal;

public class UserAuthDto {
    public Integer userId;
    public String email;
    public String fullName;
    public String passwordHash;
    public boolean isActive;

    public UserAuthDto(Integer userId, String email, String fullName, String passwordHash, boolean isActive) {
        this.userId = userId;
        this.email = email;  
        this.fullName = fullName;      
        this.passwordHash = passwordHash;
        this.isActive = isActive;
    }
}