package com.cocoaromas.api.entrypoints.rest.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocoaromas.api.application.port.in.admin.AdminPromotionsUseCase;
import com.cocoaromas.api.application.service.admin.AdminPromotionValidationException;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotion;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionPage;
import com.cocoaromas.api.domain.admin.promotion.DiscountType;
import com.cocoaromas.api.domain.admin.promotion.PromotionType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminPromotionController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminPromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminPromotionsUseCase adminPromotionsUseCase;

    @Test
    void shouldListPromotions() throws Exception {
        given(adminPromotionsUseCase.list(any())).willReturn(new AdminPromotionPage(
                List.of(promotion(1L)),
                0,
                20,
                1,
                1
        ));

        mockMvc.perform(get("/api/v1/admin/promotions").param("search", "promo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[0].promotionType").value("PRODUCT"));
    }

    @Test
    void shouldCreatePromotion() throws Exception {
        given(adminPromotionsUseCase.create(any())).willReturn(promotion(9L));

        mockMvc.perform(post("/api/v1/admin/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Promo perfumes",
                                  "promotionType": "PRODUCT",
                                  "discountType": "PERCENTAGE",
                                  "discountValue": 10,
                                  "productId": 4,
                                  "active": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(9));
    }

    @Test
    void shouldUpdatePromotion() throws Exception {
        given(adminPromotionsUseCase.update(any(), any())).willReturn(promotion(3L));

        mockMvc.perform(put("/api/v1/admin/promotions/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Promo perfumes",
                                  "promotionType": "PRODUCT",
                                  "discountType": "FIXED_AMOUNT",
                                  "discountValue": 2000,
                                  "productId": 4,
                                  "active": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountType").value("PERCENTAGE"));
    }

    @Test
    void shouldUpdatePromotionStatus() throws Exception {
        given(adminPromotionsUseCase.updateStatus(3L, false)).willReturn(promotion(3L));

        mockMvc.perform(patch("/api/v1/admin/promotions/3/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "active": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void shouldDeletePromotion() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/promotions/7"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenInvalidPromotionPayload() throws Exception {
        given(adminPromotionsUseCase.create(any())).willThrow(new AdminPromotionValidationException("El nombre es obligatorio"));

        mockMvc.perform(post("/api/v1/admin/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Promo inválida",
                                  "promotionType": "PRODUCT",
                                  "discountType": "PERCENTAGE",
                                  "discountValue": 0,
                                  "productId": 4
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    private AdminPromotion promotion(Long id) {
        return new AdminPromotion(
                id,
                "Promo perfumes",
                null,
                PromotionType.PRODUCT,
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00"),
                null,
                4L,
                "Perfume",
                null,
                null,
                true,
                OffsetDateTime.parse("2026-01-01T00:00:00Z"),
                OffsetDateTime.parse("2026-01-31T00:00:00Z"),
                OffsetDateTime.parse("2025-12-31T00:00:00Z"),
                OffsetDateTime.parse("2026-01-01T00:00:00Z")
        );
    }
}
