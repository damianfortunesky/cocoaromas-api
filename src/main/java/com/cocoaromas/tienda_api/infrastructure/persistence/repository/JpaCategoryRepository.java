package com.cocoaromas.tienda_api.infrastructure.persistence.repository;

import com.cocoaromas.tienda_api.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    List<CategoryEntity> findByActiveTrue();
}
