package com.cocoaromas.api.entrypoints.rest.order;

import com.cocoaromas.api.application.port.in.order.CreateOrderUseCase;
import com.cocoaromas.api.application.port.out.auth.SecurityContextPort;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.CreateOrderRequest;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.CreateOrderResponse;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Operaciones de pedidos para clientes")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final SecurityContextPort securityContextPort;

    public OrderController(CreateOrderUseCase createOrderUseCase, SecurityContextPort securityContextPort) {
        this.createOrderUseCase = createOrderUseCase;
        this.securityContextPort = securityContextPort;
    }

    @PostMapping
    @Operation(
            summary = "Crea un pedido autenticado",
            description = "Recalcula precios en backend y persiste pedido + ítems. Estado inicial: TRANSFER => esperando_pago, MERCADO_PAGO => pendiente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido creado", content = @Content(schema = @Schema(implementation = CreateOrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validación inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long userId = securityContextPort.getAuthenticatedUserId();
        return CreateOrderResponse.fromDomain(createOrderUseCase.createOrder(request.toCommand(userId)));
    }
}
