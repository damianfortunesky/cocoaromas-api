package com.cocoaromas.tienda_api.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class User {
    private Integer id;
    private String email;
    private String fullName;
    private String phone;
    private boolean active;
    private LocalDateTime createdAt;
    private List<Role> roles;

    public User(Integer id, String email, String fullName, String phone, boolean active, LocalDateTime createdAt, List<Role> roles) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.active = active;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    public User() {}

    public Integer getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Role> getRoles() { return roles; }
}
