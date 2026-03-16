package com.cocoaromas.api.application.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.cocoaromas.api.application.port.out.admin.ManageAdminProductsPort;
import com.cocoaromas.api.domain.admin.AdminProduct;
import com.cocoaromas.api.domain.admin.AdminProductPage;
import com.cocoaromas.api.domain.admin.AdminProductQuery;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminProductsServiceTest {

    @Mock
    private ManageAdminProductsPort manageAdminProductsPort;

    private AdminProductsService service;

    @BeforeEach
    void setUp() {
        service = new AdminProductsService(manageAdminProductsPort);
    }

    @Test
    void shouldFallbackToFirstPageWhenRequestedPageIsOutOfRange() {
        AdminProductQuery requested = new AdminProductQuery("Sahumerio Premium", 1L, true, 50, 20, "updatedAt", "desc");

        given(manageAdminProductsPort.find(new AdminProductQuery("Sahumerio Premium", 1L, true, 50, 20, "updatedAt", "desc")))
                .willReturn(new AdminProductPage(List.of(), 50, 20, 1, 1));
        given(manageAdminProductsPort.find(new AdminProductQuery("Sahumerio Premium", 1L, true, 0, 20, "updatedAt", "desc")))
                .willReturn(new AdminProductPage(List.of(sampleItem()), 0, 20, 1, 1));

        AdminProductPage result = service.list(requested);

        assertThat(result.items()).hasSize(1);
        assertThat(result.page()).isEqualTo(0);
        verify(manageAdminProductsPort).find(new AdminProductQuery("Sahumerio Premium", 1L, true, 50, 20, "updatedAt", "desc"));
        verify(manageAdminProductsPort).find(new AdminProductQuery("Sahumerio Premium", 1L, true, 0, 20, "updatedAt", "desc"));
    }

    @Test
    void shouldNormalizeNegativePageAndInvalidSize() {
        AdminProductQuery requested = new AdminProductQuery(null, null, null, -4, 0, "updatedAt", "desc");
        given(manageAdminProductsPort.find(any())).willReturn(new AdminProductPage(List.of(), 0, 1, 0, 0));

        service.list(requested);

        verify(manageAdminProductsPort).find(new AdminProductQuery(null, null, null, 0, 1, "updatedAt", "desc"));
    }

    private AdminProduct sampleItem() {
        return new AdminProduct(
                1L,
                "Sahumerio Premium",
                "Descripción",
                new BigDecimal("12000"),
                1L,
                "Sahumerios",
                15,
                "https://img",
                true,
                OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                OffsetDateTime.parse("2026-01-01T10:00:00Z")
        );
    }
}
