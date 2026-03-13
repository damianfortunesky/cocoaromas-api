package com.cocoaromas.api.application.service.admin;

import com.cocoaromas.api.application.port.in.admin.AdminPromotionsUseCase;
import com.cocoaromas.api.application.port.out.admin.ManageAdminPromotionsPort;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotion;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionPage;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionQuery;
import com.cocoaromas.api.domain.admin.promotion.DiscountType;
import com.cocoaromas.api.domain.admin.promotion.PromotionType;
import com.cocoaromas.api.domain.admin.promotion.UpsertAdminPromotionCommand;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class AdminPromotionsService implements AdminPromotionsUseCase {

    private final ManageAdminPromotionsPort manageAdminPromotionsPort;

    public AdminPromotionsService(ManageAdminPromotionsPort manageAdminPromotionsPort) {
        this.manageAdminPromotionsPort = manageAdminPromotionsPort;
    }

    @Override
    public AdminPromotionPage list(AdminPromotionQuery query) {
        return manageAdminPromotionsPort.find(query);
    }

    @Override
    public AdminPromotion create(UpsertAdminPromotionCommand command) {
        validate(command);
        AdminPromotion promotion = new AdminPromotion(
                null,
                command.name().trim(),
                normalize(command.description()),
                command.promotionType(),
                command.discountType(),
                command.discountValue(),
                command.minimumQuantity(),
                command.productId(),
                null,
                command.categoryId(),
                null,
                command.active() == null || command.active(),
                command.startsAt(),
                command.endsAt(),
                null,
                null
        );
        return manageAdminPromotionsPort.save(promotion);
    }

    @Override
    public AdminPromotion update(Long id, UpsertAdminPromotionCommand command) {
        validate(command);
        AdminPromotion current = manageAdminPromotionsPort.getById(id);
        AdminPromotion promotion = new AdminPromotion(
                current.id(),
                command.name().trim(),
                normalize(command.description()),
                command.promotionType(),
                command.discountType(),
                command.discountValue(),
                command.minimumQuantity(),
                command.productId(),
                null,
                command.categoryId(),
                null,
                command.active() == null ? current.active() : command.active(),
                command.startsAt(),
                command.endsAt(),
                current.createdAt(),
                null
        );
        return manageAdminPromotionsPort.save(promotion);
    }

    @Override
    public AdminPromotion updateStatus(Long id, boolean active) {
        AdminPromotion current = manageAdminPromotionsPort.getById(id);
        return manageAdminPromotionsPort.save(new AdminPromotion(
                current.id(),
                current.name(),
                current.description(),
                current.promotionType(),
                current.discountType(),
                current.discountValue(),
                current.minimumQuantity(),
                current.productId(),
                current.productName(),
                current.categoryId(),
                current.categoryName(),
                active,
                current.startsAt(),
                current.endsAt(),
                current.createdAt(),
                null
        ));
    }

    @Override
    public void delete(Long id) {
        manageAdminPromotionsPort.softDelete(id);
    }

    private void validate(UpsertAdminPromotionCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new AdminPromotionValidationException("El nombre es obligatorio");
        }
        if (command.promotionType() == null) {
            throw new AdminPromotionValidationException("El tipo de promoción es obligatorio");
        }
        if (command.discountType() == null) {
            throw new AdminPromotionValidationException("El tipo de descuento es obligatorio");
        }
        if (command.discountValue() == null || command.discountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AdminPromotionValidationException("El valor de descuento debe ser mayor a 0");
        }
        if (command.discountType() == DiscountType.PERCENTAGE && command.discountValue().compareTo(new BigDecimal("100")) > 0) {
            throw new AdminPromotionValidationException("El descuento porcentual no puede superar 100");
        }
        if (command.startsAt() != null && command.endsAt() != null && command.endsAt().isBefore(command.startsAt())) {
            throw new AdminPromotionValidationException("La fecha fin no puede ser anterior a la fecha inicio");
        }

        if (command.promotionType() == PromotionType.QUANTITY) {
            if (command.minimumQuantity() == null || command.minimumQuantity() < 1) {
                throw new AdminPromotionValidationException("La promoción por cantidad requiere minimumQuantity válido");
            }
            if (command.productId() == null) {
                throw new AdminPromotionValidationException("La promoción por cantidad requiere productId");
            }
            if (!manageAdminPromotionsPort.existsProduct(command.productId())) {
                throw new AdminPromotionValidationException("El producto objetivo no existe");
            }
            if (command.categoryId() != null) {
                throw new AdminPromotionValidationException("La promoción por cantidad no admite categoryId");
            }
            return;
        }

        if (command.minimumQuantity() != null) {
            throw new AdminPromotionValidationException("minimumQuantity solo aplica a promociones por cantidad");
        }

        if (command.promotionType() == PromotionType.PRODUCT) {
            if (command.productId() == null) {
                throw new AdminPromotionValidationException("La promoción por producto requiere productId");
            }
            if (!manageAdminPromotionsPort.existsProduct(command.productId())) {
                throw new AdminPromotionValidationException("El producto objetivo no existe");
            }
            if (command.categoryId() != null) {
                throw new AdminPromotionValidationException("La promoción por producto no admite categoryId");
            }
            return;
        }

        if (command.promotionType() == PromotionType.CATEGORY) {
            if (command.categoryId() == null) {
                throw new AdminPromotionValidationException("La promoción por categoría requiere categoryId");
            }
            if (!manageAdminPromotionsPort.existsCategory(command.categoryId())) {
                throw new AdminPromotionValidationException("La categoría objetivo no existe");
            }
            if (command.productId() != null) {
                throw new AdminPromotionValidationException("La promoción por categoría no admite productId");
            }
        }
    }

    private String normalize(String description) {
        return description == null || description.isBlank() ? null : description.trim();
    }
}
