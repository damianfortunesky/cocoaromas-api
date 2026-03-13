package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.domain.admin.promotion.AdminPromotion;
import com.cocoaromas.api.domain.admin.promotion.DiscountType;
import com.cocoaromas.api.domain.admin.promotion.PromotionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class AdminPromotionDtos {

    private AdminPromotionDtos() {
    }

    @Schema(name = "AdminPromotionsPageResponse")
    public record PromotionsPageResponse(
            List<PromotionListItemResponse> items,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        static PromotionsPageResponse fromDomain(com.cocoaromas.api.domain.admin.promotion.AdminPromotionPage page) {
            return new PromotionsPageResponse(
                    page.items().stream().map(PromotionListItemResponse::fromDomain).toList(),
                    page.page(),
                    page.size(),
                    page.totalElements(),
                    page.totalPages()
            );
        }
    }

    @Schema(name = "AdminPromotionListItemResponse")
    public record PromotionListItemResponse(
            Long id,
            String name,
            PromotionType promotionType,
            DiscountType discountType,
            BigDecimal discountValue,
            boolean active,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt
    ) {
        static PromotionListItemResponse fromDomain(AdminPromotion promotion) {
            return new PromotionListItemResponse(
                    promotion.id(),
                    promotion.name(),
                    promotion.promotionType(),
                    promotion.discountType(),
                    promotion.discountValue(),
                    promotion.active(),
                    promotion.startsAt(),
                    promotion.endsAt()
            );
        }
    }

    @Schema(name = "AdminPromotionDetailResponse")
    public record PromotionDetailResponse(
            Long id,
            String name,
            String description,
            PromotionType promotionType,
            DiscountType discountType,
            BigDecimal discountValue,
            Integer minimumQuantity,
            Long productId,
            String productName,
            Long categoryId,
            String categoryName,
            boolean active,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        static PromotionDetailResponse fromDomain(AdminPromotion promotion) {
            return new PromotionDetailResponse(
                    promotion.id(),
                    promotion.name(),
                    promotion.description(),
                    promotion.promotionType(),
                    promotion.discountType(),
                    promotion.discountValue(),
                    promotion.minimumQuantity(),
                    promotion.productId(),
                    promotion.productName(),
                    promotion.categoryId(),
                    promotion.categoryName(),
                    promotion.active(),
                    promotion.startsAt(),
                    promotion.endsAt(),
                    promotion.createdAt(),
                    promotion.updatedAt()
            );
        }
    }

    @Schema(name = "AdminUpsertPromotionRequest")
    public record UpsertPromotionRequest(
            @NotBlank String name,
            String description,
            @NotNull PromotionType promotionType,
            @NotNull DiscountType discountType,
            @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal discountValue,
            Integer minimumQuantity,
            Long productId,
            Long categoryId,
            Boolean active,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt
    ) {
    }

    @Schema(name = "AdminUpdatePromotionStatusRequest")
    public record UpdateStatusRequest(@NotNull Boolean active) {
    }

    @Schema(name = "AdminPromotionErrorResponse")
    public record ErrorResponse(String code, String message) {
    }
}
