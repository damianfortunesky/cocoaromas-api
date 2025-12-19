package com.cocoaromas.tienda_api.application.dto.internal;

public class CreatedUserDto {
    public Integer userId;
    public String email;

    public CreatedUserDto(Integer userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}