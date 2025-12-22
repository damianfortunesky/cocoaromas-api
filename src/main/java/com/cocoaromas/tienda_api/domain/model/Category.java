package com.cocoaromas.tienda_api.domain.model;

public class Category {
 
    private Integer id;
    private String name;
    private String description;
    private boolean active;
    
    public Category(Integer id, String name, String description, boolean active) {
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
