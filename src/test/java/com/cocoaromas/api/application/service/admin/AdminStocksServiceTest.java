package com.cocoaromas.api.application.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.cocoaromas.api.application.port.out.admin.ManageAdminStocksPort;
import com.cocoaromas.api.domain.admin.stock.AdminStockDetail;
import com.cocoaromas.api.domain.admin.stock.UpdateAdminStockCommand;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminStocksServiceTest {

    @Mock
    private ManageAdminStocksPort manageAdminStocksPort;

    private AdminStocksService service;

    @BeforeEach
    void setUp() {
        service = new AdminStocksService(manageAdminStocksPort, 5);
    }

    @Test
    void shouldSetStockUsingAbsoluteQuantity() {
        given(manageAdminStocksPort.getByProductId(10L, 5)).willReturn(detail(10L, 7));
        given(manageAdminStocksPort.updateSimpleStock(10L, 12, 5)).willReturn(detail(10L, 12));

        AdminStockDetail updated = service.updateStock(10L, new UpdateAdminStockCommand(12, null, "Ajuste inventario"));

        assertThat(updated.stockQuantity()).isEqualTo(12);
        verify(manageAdminStocksPort).updateSimpleStock(10L, 12, 5);
    }

    @Test
    void shouldAdjustStockIncrementally() {
        given(manageAdminStocksPort.getByProductId(10L, 5)).willReturn(detail(10L, 7));
        given(manageAdminStocksPort.updateSimpleStock(10L, 4, 5)).willReturn(detail(10L, 4));

        AdminStockDetail updated = service.updateStock(10L, new UpdateAdminStockCommand(null, -3, "Conteo"));

        assertThat(updated.stockQuantity()).isEqualTo(4);
        verify(manageAdminStocksPort).updateSimpleStock(10L, 4, 5);
    }

    @Test
    void shouldRejectNegativeResultingStock() {
        given(manageAdminStocksPort.getByProductId(10L, 5)).willReturn(detail(10L, 2));

        assertThatThrownBy(() -> service.updateStock(10L, new UpdateAdminStockCommand(null, -3, null)))
                .isInstanceOf(AdminStockValidationException.class)
                .hasMessageContaining("no puede ser negativo");
    }

    @Test
    void shouldRejectPayloadWithSetAndAdjustment() {
        assertThatThrownBy(() -> service.updateStock(10L, new UpdateAdminStockCommand(8, 1, null)))
                .isInstanceOf(AdminStockValidationException.class)
                .hasMessageContaining("exactamente uno");
    }

    private AdminStockDetail detail(Long id, int quantity) {
        return new AdminStockDetail(
                id,
                "Producto",
                "Categoria",
                true,
                quantity,
                quantity > 0,
                quantity > 0 && quantity <= 5,
                false,
                "https://image",
                OffsetDateTime.parse("2026-01-01T10:00:00Z")
        );
    }
}
