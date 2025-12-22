package com.cocoaromas.tienda_api.web.controller;

import com.cocoaromas.tienda_api.application.dto.request.CreateCategoryRequest;
import com.cocoaromas.tienda_api.application.dto.response.CategoryResponse;
import com.cocoaromas.tienda_api.application.service.CategoryService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // 🔒 @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public CategoryResponse create(@RequestBody @Valid CreateCategoryRequest request) {
        return service.create(request);
    }

    @GetMapping("/active")
    public List<CategoryResponse> active() {
        return service.listActive();
    }

    @GetMapping("/all")
    public List<CategoryResponse> all() {
        return service.listAll();
    }

    @PutMapping("/deactivate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivate(@PathVariable Integer id) {
        service.desactivate(id);
    }
}