package com.cocoaromas.tienda_api.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleId")
    private Integer roleId;

    @Column(name = "Name", nullable = false, unique = true, length = 50)
    private String name;

    public Integer getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }
}