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
        return manageAdminProductsPort.find(query);
    }

    @Override
    public AdminProduct create(UpsertAdminProductCommand command) {
        validate(command);
        AdminProduct product = new AdminProduct(
                null,
                command.name(),
                command.shortDescription(),
                command.longDescription(),
                command.price(),
                command.categoryId(),
                null,
                command.mainImageUrl(),
                command.imageUrls(),
                true,
                Boolean.TRUE.equals(command.available()),
                command.attributes(),
                command.variants(),
                0,
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
                command.shortDescription(),
                command.longDescription(),
                command.price(),
                command.categoryId(),
                null,
                command.mainImageUrl(),
                command.imageUrls(),
                current.active(),
                Boolean.TRUE.equals(command.available()),
                command.attributes(),
                command.variants(),
                current.stockQuantity(),
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
                current.shortDescription(),
                current.longDescription(),
                current.price(),
                current.categoryId(),
                current.categoryName(),
                current.mainImageUrl(),
                current.imageUrls(),
                active,
                current.available(),
                current.attributes(),
                current.variants(),
                current.stockQuantity(),
                current.createdAt(),
                null
        ));
    }

    private void validate(UpsertAdminProductCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new AdminProductValidationException("El nombre es obligatorio");
        }
        if (command.price() == null || command.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new AdminProductValidationException("El precio debe ser válido y no negativo");
        }
        if (command.categoryId() == null || !manageAdminProductsPort.existsCategory(command.categoryId())) {
            throw new AdminProductValidationException("La categoría es inválida");
        }
        if (command.attributes() != null && command.attributes().keySet().stream().anyMatch(k -> k == null || k.isBlank())) {
            throw new AdminProductValidationException("Los atributos deben tener claves válidas");
        }
    }
}
