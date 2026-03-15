package com.cocoaromas.api.application.service.admin.category;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.cocoaromas.api.domain.admin.category.UpsertAdminCategoryCommand;
import com.cocoaromas.api.infrastructure.persistence.repository.catalog.CategoryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminCategoriesServiceTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    private AdminCategoriesService service;

    @BeforeEach
    void setUp() {
        service = new AdminCategoriesService(categoryJpaRepository);
    }

    @Test
    void shouldRejectCreateWhenSlugAlreadyExists() {
        given(categoryJpaRepository.existsBySlugIgnoreCase("sahumerios")).willReturn(true);

        assertThatThrownBy(() -> service.create(new UpsertAdminCategoryCommand("sahumerios", "Sahumerios", 0)))
                .isInstanceOf(AdminCategoryValidationException.class)
                .hasMessageContaining("slug ya existe");
    }

    @Test
    void shouldRejectCreateWhenNameAlreadyExists() {
        given(categoryJpaRepository.existsBySlugIgnoreCase("sahumerios")).willReturn(false);
        given(categoryJpaRepository.existsByNameIgnoreCase("sahumerios")).willReturn(true);

        assertThatThrownBy(() -> service.create(new UpsertAdminCategoryCommand("sahumerios", "Sahumerios", 0)))
                .isInstanceOf(AdminCategoryValidationException.class)
                .hasMessageContaining("name ya existe");
    }

    @Test
    void shouldRejectUpdateWhenSlugAlreadyExistsInAnotherCategory() {
        given(categoryJpaRepository.existsBySlugIgnoreCaseAndIdNot("sahumerios", 7L)).willReturn(true);

        assertThatThrownBy(() -> service.update(7L, new UpsertAdminCategoryCommand("sahumerios", "Sahumerios", 0)))
                .isInstanceOf(AdminCategoryValidationException.class)
                .hasMessageContaining("slug ya existe");
    }

    @Test
    void shouldRejectUpdateWhenNameAlreadyExistsInAnotherCategory() {
        given(categoryJpaRepository.existsBySlugIgnoreCaseAndIdNot("sahumerios", 7L)).willReturn(false);
        given(categoryJpaRepository.existsByNameIgnoreCaseAndIdNot("sahumerios", 7L)).willReturn(true);

        assertThatThrownBy(() -> service.update(7L, new UpsertAdminCategoryCommand("sahumerios", "Sahumerios", 0)))
                .isInstanceOf(AdminCategoryValidationException.class)
                .hasMessageContaining("name ya existe");
    }
}
