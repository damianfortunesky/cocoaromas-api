package com.cocoaromas.api.application.service.admin;

import com.cocoaromas.api.application.port.in.admin.AdminProductsUseCase;
import com.cocoaromas.api.application.port.out.admin.ManageAdminProductsPort;
import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import com.cocoaromas.api.domain.admin.AdminProductQuery;
import com.cocoaromas.api.domain.admin.UpsertAdminProductCommand;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class AdminProductsService implements AdminProductsUseCase {

    private final ManageAdminProductsPort manageAdminProductsPort;

    public AdminProductsService(ManageAdminProductsPort manageAdminProductsPort) {
        this.manageAdminProductsPort = manageAdminProductsPort;
    }

    @Override
    public AdminProductPage list(AdminProductQuery query) {
        AdminProductQuery normalizedQuery = normalizeQuery(query);
        AdminProductPage page = manageAdminProductsPort.find(normalizedQuery);

        if (normalizedQuery.page() > 0 && page.totalElements() > 0 && page.items().isEmpty()) {
            return manageAdminProductsPort.find(new AdminProductQuery(
                    normalizedQuery.search(),
                    normalizedQuery.categoryId(),
                    normalizedQuery.active(),
                    0,
                    normalizedQuery.size(),
                    normalizedQuery.sortBy(),
                    normalizedQuery.direction()
            ));
        }

        return page;
    }

    @Override
    public AdminProduct create(UpsertAdminProductCommand command) {
        validate(command);
        AdminProduct product = new AdminProduct(
                null,
                command.name(),
                command.description(),
                command.price(),
                command.categoryId(),
                null,
                command.stockQuantity(),
                command.imageUrl(),
                command.isActive() == null || command.isActive(),
                null,
                null
        );
        return manageAdminProductsPort.save(product);
    }

    @Override
    public AdminProduct update(Long id, UpsertAdminProductCommand command) {
        validate(command);
        AdminProduct current = manageAdminProductsPort.getById(id);
        AdminProduct product = new AdminProduct(
                current.id(),
                command.name(),
                command.description(),
                command.price(),
                command.categoryId(),
                null,
                command.stockQuantity(),
                command.imageUrl(),
                command.isActive() == null ? current.isActive() : command.isActive(),
                current.createdAt(),
                null
        );
        return manageAdminProductsPort.save(product);
    }

    @Override
    public void delete(Long id) {
        manageAdminProductsPort.softDelete(id);
    }

    @Override
    public AdminProduct updateStatus(Long id, boolean active) {
        AdminProduct current = manageAdminProductsPort.getById(id);
        return manageAdminProductsPort.save(new AdminProduct(
                current.id(),
                current.name(),
                current.description(),
                current.price(),
                current.categoryId(),
                current.categoryName(),
                current.stockQuantity(),
                current.imageUrl(),
                active,
                current.createdAt(),
                null
        ));
    }


    private AdminProductQuery normalizeQuery(AdminProductQuery query) {
        int normalizedPage = Math.max(query.page(), 0);
        int normalizedSize = Math.min(Math.max(query.size(), 1), 100);
        return new AdminProductQuery(
                query.search(),
                query.categoryId(),
                query.active(),
                normalizedPage,
                normalizedSize,
                query.sortBy(),
                query.direction()
        );
    }

    private void validate(UpsertAdminProductCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new AdminProductValidationException("El nombre es obligatorio");
        }
        if (command.description() == null || command.description().isBlank()) {
            throw new AdminProductValidationException("La descripción es obligatoria");
        }
        if (command.price() == null || command.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new AdminProductValidationException("El precio debe ser válido y no negativo");
        }
        if (command.stockQuantity() == null || command.stockQuantity() < 0) {
            throw new AdminProductValidationException("El stock debe ser válido y no negativo");
        }
        if (command.categoryId() == null || !manageAdminProductsPort.existsCategory(command.categoryId())) {
            throw new AdminProductValidationException("La categoría es inválida");
        }
    }
}
