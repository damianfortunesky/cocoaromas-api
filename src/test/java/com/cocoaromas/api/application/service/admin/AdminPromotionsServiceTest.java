package com.cocoaromas.api.application.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.cocoaromas.api.application.port.out.admin.ManageAdminPromotionsPort;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotion;
import com.cocoaromas.api.domain.admin.promotion.DiscountType;
import com.cocoaromas.api.domain.admin.promotion.PromotionType;
import com.cocoaromas.api.domain.admin.promotion.UpsertAdminPromotionCommand;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminPromotionsServiceTest {

    @Mock
    private ManageAdminPromotionsPort manageAdminPromotionsPort;

    private AdminPromotionsService service;

    @BeforeEach
    void setUp() {
        service = new AdminPromotionsService(manageAdminPromotionsPort);
    }

    @Test
    void shouldCreateQuantityPromotionWhenPayloadIsValid() {
        given(manageAdminPromotionsPort.existsProduct(10L)).willReturn(true);
        given(manageAdminPromotionsPort.save(org.mockito.ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        AdminPromotion created = service.create(new UpsertAdminPromotionCommand(
                "3x2 Sahumerios",
                "Llevando 3, 10% off",
                PromotionType.QUANTITY,
                DiscountType.PERCENTAGE,
                new BigDecimal("10"),
                3,
                10L,
                null,
                true,
                null,
                null
        ));

        assertThat(created.promotionType()).isEqualTo(PromotionType.QUANTITY);
        assertThat(created.minimumQuantity()).isEqualTo(3);
    }

    @Test
    void shouldRejectPercentageGreaterThan100() {
        assertThatThrownBy(() -> service.create(new UpsertAdminPromotionCommand(
                "Promo",
                null,
                PromotionType.PRODUCT,
                DiscountType.PERCENTAGE,
                new BigDecimal("150"),
                null,
                2L,
                null,
                true,
                null,
                null
        ))).isInstanceOf(AdminPromotionValidationException.class)
                .hasMessageContaining("no puede superar 100");
    }

    @Test
    void shouldRejectCategoryPromotionWithoutCategoryId() {
        assertThatThrownBy(() -> service.create(new UpsertAdminPromotionCommand(
                "Promo categoría",
                null,
                PromotionType.CATEGORY,
                DiscountType.FIXED_AMOUNT,
                new BigDecimal("2000"),
                null,
                null,
                null,
                true,
                null,
                null
        ))).isInstanceOf(AdminPromotionValidationException.class)
                .hasMessageContaining("requiere categoryId");
    }

    @Test
    void shouldRejectInvalidDateRange() {
        given(manageAdminPromotionsPort.existsProduct(2L)).willReturn(true);

        assertThatThrownBy(() -> service.create(new UpsertAdminPromotionCommand(
                "Promo producto",
                null,
                PromotionType.PRODUCT,
                DiscountType.FIXED_AMOUNT,
                new BigDecimal("2000"),
                null,
                2L,
                null,
                true,
                OffsetDateTime.parse("2026-01-10T00:00:00Z"),
                OffsetDateTime.parse("2026-01-09T00:00:00Z")
        ))).isInstanceOf(AdminPromotionValidationException.class)
                .hasMessageContaining("fecha fin");
    }
}
