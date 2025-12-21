package com.cocoaromas.tienda_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryRequest {
    
    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
