package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.port.in.admin.AdminStocksUseCase;
import com.cocoaromas.api.domain.admin.stock.AdminStockQuery;
import com.cocoaromas.api.domain.admin.stock.UpdateAdminStockCommand;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.ErrorResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminStockDtos.StockDetailResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminStockDtos.StockPageResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminStockDtos.UpdateStockRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/stocks")
@Tag(name = "Admin Stocks", description = "Gestión administrativa de stock")
public class AdminStockController {

    private final AdminStocksUseCase adminStocksUseCase;

    public AdminStockController(AdminStocksUseCase adminStocksUseCase) {
        this.adminStocksUseCase = adminStocksUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','EMPLOYEE')")
    @Operation(summary = "Listar stock administrativo", security = @SecurityRequirement(name = "bearerAuth"))
    public StockPageResponse list(
            @Parameter(description = "Búsqueda por nombre") @RequestParam(required = false) String search,
            @Parameter(description = "Filtro por categoría") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Filtro por disponibilidad") @RequestParam(required = false) Boolean available,
            @Parameter(description = "Filtro por stock bajo") @RequestParam(required = false) Boolean lowStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return StockPageResponse.fromDomain(adminStocksUseCase.list(
                new AdminStockQuery(search, categoryId, available, lowStock, page, size, sortBy, direction)
        ));
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','EMPLOYEE')")
    @Operation(summary = "Obtener stock por producto", security = @SecurityRequirement(name = "bearerAuth"))
    public StockDetailResponse getByProductId(@PathVariable Long productId) {
        return StockDetailResponse.fromDomain(adminStocksUseCase.getByProductId(productId));
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER','EMPLOYEE')")
    @Operation(summary = "Actualizar stock por producto", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado", content = @Content(schema = @Schema(implementation = StockDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public StockDetailResponse updateStock(@PathVariable Long productId, @Valid @RequestBody UpdateStockRequest request) {
        return StockDetailResponse.fromDomain(adminStocksUseCase.updateStock(productId,
                new UpdateAdminStockCommand(request.newStockQuantity(), request.adjustment(), request.reason())));
    }
}
