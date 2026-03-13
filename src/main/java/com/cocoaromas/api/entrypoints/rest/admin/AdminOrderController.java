package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.port.in.admin.AdminOrdersUseCase;
import com.cocoaromas.api.domain.admin.order.AdminOrderQuery;
import com.cocoaromas.api.domain.admin.order.UpdateOrderStatusCommand;
import com.cocoaromas.api.domain.order.OrderStatus;
import com.cocoaromas.api.domain.order.PaymentMethod;
import com.cocoaromas.api.entrypoints.rest.admin.AdminOrderDtos.AdminOrderDetailResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminOrderDtos.AdminOrderStatusUpdateRequest;
import com.cocoaromas.api.entrypoints.rest.admin.AdminOrderDtos.AdminOrdersPageResponse;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/orders")
@Tag(name = "Admin Orders", description = "Gestión administrativa de pedidos")
public class AdminOrderController {

    private final AdminOrdersUseCase adminOrdersUseCase;

    public AdminOrderController(AdminOrdersUseCase adminOrdersUseCase) {
        this.adminOrdersUseCase = adminOrdersUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(
            summary = "Listar pedidos administrativos",
            description = "Permite filtrar por cliente, estado, método de pago y rango de fechas.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido", content = @Content(schema = @Schema(implementation = AdminOrdersPageResponse.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public AdminOrdersPageResponse list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @Parameter(description = "Fecha desde en formato ISO-8601")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime createdFrom,
            @Parameter(description = "Fecha hasta en formato ISO-8601")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime createdTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return AdminOrdersPageResponse.fromDomain(adminOrdersUseCase.list(
                new AdminOrderQuery(search, status, paymentMethod, createdFrom, createdTo, page, size, sortBy, direction)
        ));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Obtener detalle administrativo de pedido", security = @SecurityRequirement(name = "bearerAuth"))
    public AdminOrderDetailResponse getById(@PathVariable Long orderId) {
        return AdminOrderDetailResponse.fromDomain(adminOrdersUseCase.getById(orderId));
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(
            summary = "Actualizar estado administrativo de pedido",
            description = "Transiciones permitidas: PENDIENTE->ESPERANDO_PAGO/CANCELADO, ESPERANDO_PAGO->PAGADO/CANCELADO, PAGADO->PREPARANDO, PREPARANDO->ENVIADO, ENVIADO->ENTREGADO.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado", content = @Content(schema = @Schema(implementation = AdminOrderDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Transición o payload inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public AdminOrderDetailResponse updateStatus(@PathVariable Long orderId,
                                                 @Valid @RequestBody AdminOrderStatusUpdateRequest request) {
        return AdminOrderDetailResponse.fromDomain(adminOrdersUseCase.updateStatus(
                orderId,
                new UpdateOrderStatusCommand(request.newStatus(), request.reason())
        ));
    }
}
