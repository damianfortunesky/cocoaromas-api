package com.cocoaromas.api.application.port.out.admin;

import com.cocoaromas.api.domain.admin.promotion.AdminPromotion;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionPage;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionQuery;

public interface ManageAdminPromotionsPort {
    AdminPromotionPage find(AdminPromotionQuery query);

    AdminPromotion save(AdminPromotion promotion);

    AdminPromotion getById(Long id);

    void softDelete(Long id);

    boolean existsProduct(Long productId);

    boolean existsCategory(Long categoryId);
}
