package com.cocoaromas.tienda_api.domain.model;

public class Role {
    private Integer id;
    private String name;

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role() {}
    public Integer getId() { return id; }
    public String getName() { return name; }
}
