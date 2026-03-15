package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.port.in.admin.AdminProductsUseCase;
import com.cocoaromas.api.domain.admin.AdminProductQuery;
import com.cocoaromas.api.domain.admin.UpsertAdminProductCommand;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.AdminProductDetailResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.ErrorResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.ProductsPageResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.UpdateStatusRequest;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.UpsertProductRequest;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@Tag(name = "Admin Products", description = "CRUD administrativo de productos")
public class AdminProductController {

    private final AdminProductsUseCase adminProductsUseCase;

    public AdminProductController(AdminProductsUseCase adminProductsUseCase) {
        this.adminProductsUseCase = adminProductsUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar productos administrativos", security = @SecurityRequirement(name = "bearerAuth"))
    public ProductsPageResponse list(
            @Parameter(description = "Búsqueda por nombre") @RequestParam(required = false) String search,
            @Parameter(description = "Filtro por categoría") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Filtro de activo") @RequestParam(required = false) Boolean active,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String direction
    ) {
        return ProductsPageResponse.fromDomain(adminProductsUseCase.list(new AdminProductQuery(search, categoryId, active, page, size, sortBy, direction)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear producto", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado", content = @Content(schema = @Schema(implementation = AdminProductDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public AdminProductDetailResponse create(@Valid @RequestBody UpsertProductRequest request) {
        return AdminProductDetailResponse.fromDomain(adminProductsUseCase.create(toCommand(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Editar producto", security = @SecurityRequirement(name = "bearerAuth"))
    public AdminProductDetailResponse update(@PathVariable Long id, @Valid @RequestBody UpsertProductRequest request) {
        return AdminProductDetailResponse.fromDomain(adminProductsUseCase.update(id, toCommand(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar producto (soft delete)", security = @SecurityRequirement(name = "bearerAuth"))
    public void delete(@PathVariable Long id) {
        adminProductsUseCase.delete(id);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar o desactivar producto", security = @SecurityRequirement(name = "bearerAuth"))
    public AdminProductDetailResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return AdminProductDetailResponse.fromDomain(adminProductsUseCase.updateStatus(id, request.active()));
    }

    private UpsertAdminProductCommand toCommand(UpsertProductRequest request) {
        return new UpsertAdminProductCommand(
                request.name(),
                request.description(),
                request.price(),
                request.categoryId(),
                request.stockQuantity(),
                request.imageUrl(),
                request.isActive()
        );
    }
}
