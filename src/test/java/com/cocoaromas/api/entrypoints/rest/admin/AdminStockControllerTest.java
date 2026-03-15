package com.cocoaromas.api.entrypoints.rest.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocoaromas.api.application.port.in.admin.AdminStocksUseCase;
import com.cocoaromas.api.application.service.admin.AdminStockValidationException;
import com.cocoaromas.api.domain.admin.stock.AdminStockDetail;
import com.cocoaromas.api.domain.admin.stock.AdminStockItem;
import com.cocoaromas.api.domain.admin.stock.AdminStockPage;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminStockController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminStocksUseCase adminStocksUseCase;

    @Test
    void shouldListAdminStocks() throws Exception {
        given(adminStocksUseCase.list(any())).willReturn(new AdminStockPage(
                List.of(new AdminStockItem(3L, "Sahumerio", "Sahumerios", true, 4, true, true, "https://img")),
                0,
                20,
                1,
                1
        ));

        mockMvc.perform(get("/api/v1/admin/stocks")
                        .param("search", "sahu")
                        .param("lowStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(3))
                .andExpect(jsonPath("$.items[0].lowStock").value(true));
    }

    @Test
    void shouldGetStockByProductId() throws Exception {
        given(adminStocksUseCase.getByProductId(10L)).willReturn(new AdminStockDetail(
                10L,
                "Producto",
                "Perfumes",
                true,
                0,
                false,
                false,
                "https://img",
                OffsetDateTime.parse("2026-01-01T10:00:00Z")
        ));

        mockMvc.perform(get("/api/v1/admin/stocks/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(10))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void shouldPatchStock() throws Exception {
        given(adminStocksUseCase.updateStock(any(), any())).willReturn(new AdminStockDetail(
                8L,
                "Producto",
                "Perfumes",
                true,
                11,
                true,
                false,
                "https://img",
                OffsetDateTime.parse("2026-01-01T10:00:00Z")
        ));

        mockMvc.perform(patch("/api/v1/admin/stocks/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newStockQuantity": 11,
                                  "reason": "Conteo"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(11));
    }

    @Test
    void shouldReturn400WhenStockPayloadIsInvalid() throws Exception {
        given(adminStocksUseCase.updateStock(any(), any())).willThrow(new AdminStockValidationException("Debes enviar exactamente uno"));

        mockMvc.perform(patch("/api/v1/admin/stocks/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "newStockQuantity": 11,
                                  "adjustment": 3
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
