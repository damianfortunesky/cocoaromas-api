package com.cocoaromas.api.application.port.in.admin;

import com.cocoaromas.api.domain.admin.promotion.AdminPromotion;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionPage;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionQuery;
import com.cocoaromas.api.domain.admin.promotion.UpsertAdminPromotionCommand;

public interface AdminPromotionsUseCase {
    AdminPromotionPage list(AdminPromotionQuery query);

    AdminPromotion create(UpsertAdminPromotionCommand command);

    AdminPromotion update(Long id, UpsertAdminPromotionCommand command);

    AdminPromotion updateStatus(Long id, boolean active);

    void delete(Long id);
}
