package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.port.in.admin.AdminPromotionsUseCase;
import com.cocoaromas.api.domain.admin.promotion.AdminPromotionQuery;
import com.cocoaromas.api.domain.admin.promotion.UpsertAdminPromotionCommand;
import com.cocoaromas.api.entrypoints.rest.admin.AdminPromotionDtos.ErrorResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminPromotionDtos.PromotionDetailResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminPromotionDtos.PromotionsPageResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminPromotionDtos.UpdateStatusRequest;
import com.cocoaromas.api.entrypoints.rest.admin.AdminPromotionDtos.UpsertPromotionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/promotions")
@Tag(name = "Admin Promotions", description = "CRUD administrativo de promociones")
public class AdminPromotionController {

    private final AdminPromotionsUseCase adminPromotionsUseCase;

    public AdminPromotionController(AdminPromotionsUseCase adminPromotionsUseCase) {
        this.adminPromotionsUseCase = adminPromotionsUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar promociones administrativas", security = @SecurityRequirement(name = "bearerAuth"))
    public PromotionsPageResponse list(
            @Parameter(description = "Búsqueda por nombre") @RequestParam(required = false) String search,
            @RequestParam(required = false) com.cocoaromas.api.domain.admin.promotion.PromotionType promotionType,
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Filtra promociones vigentes al momento actual") @RequestParam(required = false) Boolean currentlyValid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return PromotionsPageResponse.fromDomain(adminPromotionsUseCase.list(
                new AdminPromotionQuery(search, promotionType, active, currentlyValid, page, size, sortBy, direction)
        ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear promoción", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creada", content = @Content(schema = @Schema(implementation = PromotionDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public PromotionDetailResponse create(@Valid @RequestBody UpsertPromotionRequest request) {
        return PromotionDetailResponse.fromDomain(adminPromotionsUseCase.create(toCommand(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Editar promoción", security = @SecurityRequirement(name = "bearerAuth"))
    public PromotionDetailResponse update(@PathVariable Long id, @Valid @RequestBody UpsertPromotionRequest request) {
        return PromotionDetailResponse.fromDomain(adminPromotionsUseCase.update(id, toCommand(request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar o desactivar promoción", security = @SecurityRequirement(name = "bearerAuth"))
    public PromotionDetailResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return PromotionDetailResponse.fromDomain(adminPromotionsUseCase.updateStatus(id, request.active()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar promoción (soft delete)", security = @SecurityRequirement(name = "bearerAuth"))
    public void delete(@PathVariable Long id) {
        adminPromotionsUseCase.delete(id);
    }

    private UpsertAdminPromotionCommand toCommand(UpsertPromotionRequest request) {
        return new UpsertAdminPromotionCommand(
                request.name(),
                request.description(),
                request.promotionType(),
                request.discountType(),
                request.discountValue(),
                request.minimumQuantity(),
                request.productId(),
                request.categoryId(),
                request.active(),
                request.startsAt(),
                request.endsAt()
        );
    }
}
