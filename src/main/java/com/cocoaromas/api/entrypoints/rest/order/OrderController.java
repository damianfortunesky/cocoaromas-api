package com.cocoaromas.api.entrypoints.rest.order;

import com.cocoaromas.api.application.port.in.order.CreateOrderUseCase;
import com.cocoaromas.api.application.port.in.order.GetMyOrderDetailUseCase;
import com.cocoaromas.api.application.port.in.order.GetMyOrdersUseCase;
import com.cocoaromas.api.application.port.out.auth.SecurityContextPort;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.CreateOrderRequest;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.CreateOrderResponse;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.ErrorResponse;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.MyOrderDetailResponse;
import com.cocoaromas.api.entrypoints.rest.order.OrderDtos.MyOrdersPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Operaciones de pedidos para clientes")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetMyOrdersUseCase getMyOrdersUseCase;
    private final GetMyOrderDetailUseCase getMyOrderDetailUseCase;
    private final SecurityContextPort securityContextPort;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetMyOrdersUseCase getMyOrdersUseCase,
            GetMyOrderDetailUseCase getMyOrderDetailUseCase,
            SecurityContextPort securityContextPort
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getMyOrdersUseCase = getMyOrdersUseCase;
        this.getMyOrderDetailUseCase = getMyOrderDetailUseCase;
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

    @GetMapping("/me")
    @Operation(
            summary = "Lista los pedidos del usuario autenticado",
            description = "Retorna pedidos del cliente actual con paginación.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido", content = @Content(schema = @Schema(implementation = MyOrdersPageResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public MyOrdersPageResponse getMyOrders(
            @Parameter(description = "Página base cero")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Long userId = securityContextPort.getAuthenticatedUserId();
        return MyOrdersPageResponse.fromDomain(getMyOrdersUseCase.getMyOrders(userId, page, size));
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Obtiene detalle de un pedido propio",
            description = "Si el pedido existe pero pertenece a otro usuario, responde 404 para evitar enumeración de pedidos.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle obtenido", content = @Content(schema = @Schema(implementation = MyOrderDetailResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public MyOrderDetailResponse getMyOrderDetail(
            @Parameter(description = "ID del pedido", required = true)
            @PathVariable Long orderId
    ) {
        Long userId = securityContextPort.getAuthenticatedUserId();
        return MyOrderDetailResponse.fromDomain(getMyOrderDetailUseCase.getMyOrderDetail(userId, orderId));
    }
}
