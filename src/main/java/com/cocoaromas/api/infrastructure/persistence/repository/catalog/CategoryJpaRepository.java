package com.cocoaromas.api.infrastructure.persistence.repository.catalog;

import com.cocoaromas.api.infrastructure.persistence.entity.catalog.CategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findAllByOrderByDisplayOrderAscNameAsc();

    boolean existsBySlugIgnoreCase(String slug);

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
