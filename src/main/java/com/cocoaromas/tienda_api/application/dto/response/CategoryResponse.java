package com.cocoaromas.tienda_api.application.dto.response;

public class CategoryResponse {

    public Integer id;
    public String name;
    public String description;
    public boolean active;

    public CategoryResponse(Integer id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

}
