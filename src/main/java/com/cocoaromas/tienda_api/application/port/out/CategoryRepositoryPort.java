package com.cocoaromas.tienda_api.application.port.out;

import com.cocoaromas.tienda_api.domain.model.Category;
import java.util.List;

public interface CategoryRepositoryPort { 

    Category save(String name, String description);

    List<Category> findActive();

    List<Category> findAll();

    void desactivate(Integer categoryId);

}
