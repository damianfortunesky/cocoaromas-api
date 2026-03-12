package com.cocoaromas.api.entrypoints.rest.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocoaromas.api.application.port.in.catalog.GetPublicCategoriesUseCase;
import com.cocoaromas.api.application.port.in.catalog.GetPublicProductDetailUseCase;
import com.cocoaromas.api.application.port.in.catalog.GetPublicProductsUseCase;
import com.cocoaromas.api.application.service.catalog.ProductNotFoundException;
import com.cocoaromas.api.domain.catalog.ProductCatalogPage;
import com.cocoaromas.api.domain.catalog.ProductCategory;
import com.cocoaromas.api.domain.catalog.ProductDetail;
import com.cocoaromas.api.domain.catalog.ProductSummary;
import com.cocoaromas.api.domain.catalog.ProductVariant;
import com.cocoaromas.api.domain.catalog.RelatedProduct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CatalogController.class)
@AutoConfigureMockMvc(addFilters = false)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPublicProductsUseCase getPublicProductsUseCase;

    @MockBean
    private GetPublicCategoriesUseCase getPublicCategoriesUseCase;

    @MockBean
    private GetPublicProductDetailUseCase getPublicProductDetailUseCase;

    @Test
    void shouldListProductsForCatalog() throws Exception {
        ProductCategory category = new ProductCategory(1L, "sahumerios", "Sahumerios");
        ProductSummary product = new ProductSummary(
                10L,
                "Sahumerio Sagrada Madre Lavanda",
                "Blend relajante de lavanda y hierbas.",
                new BigDecimal("3500.00"),
                category,
                "https://images.mock.cocoaromas/sahumerio-lavanda.jpg",
                false,
                0
        );

        given(getPublicProductsUseCase.getProducts(any()))
                .willReturn(new ProductCatalogPage(List.of(product), 0, 12, 1, 1));

        mockMvc.perform(get("/api/v1/products")
                        .param("search", "lavanda")
                        .param("category", "sahumerios")
                        .param("sort", "price,desc")
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(10))
                .andExpect(jsonPath("$.items[0].available").value(false))
                .andExpect(jsonPath("$.items[0].stockQuantity").value(0))
                .andExpect(jsonPath("$.items[0].category.slug").value("sahumerios"));
    }

    @Test
    void shouldGetProductDetail() throws Exception {
        ProductCategory category = new ProductCategory(3L, "remeras", "Remeras");
        ProductDetail detail = new ProductDetail(
                5L,
                "Remera Oversize Cocoaromas Negra",
                "Remera unisex de algodón premium.",
                "Remera oversize con calce relajado.",
                new BigDecimal("16500.00"),
                category,
                "https://images.mock.cocoaromas/remera-oversize-negra.jpg",
                List.of(
                        "https://images.mock.cocoaromas/remera-oversize-negra.jpg",
                        "https://images.mock.cocoaromas/remera-oversize-negra-2.jpg"
                ),
                true,
                6,
                Map.of("material", "Algodón", "color", "Negro"),
                List.of(new ProductVariant("REM-OVR-BLK-S", "Talle S", Map.of("talle", "S", "color", "Negro"), 2, true)),
                List.of(new RelatedProduct(99L, "Remera Blanca", new BigDecimal("15000.00"), "https://images.mock.cocoaromas/remera-blanca.jpg", true))
        );

        given(getPublicProductDetailUseCase.getProductDetail(5L)).willReturn(detail);

        mockMvc.perform(get("/api/v1/products/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.longDescription").value("Remera oversize con calce relajado."))
                .andExpect(jsonPath("$.attributes.material").value("Algodón"))
                .andExpect(jsonPath("$.variants[0].id").value("REM-OVR-BLK-S"))
                .andExpect(jsonPath("$.relatedProducts[0].id").value(99));
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {
        given(getPublicProductDetailUseCase.getProductDetail(999L)).willThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    void shouldListCategoriesForCatalog() throws Exception {
        given(getPublicCategoriesUseCase.getCategories())
                .willReturn(List.of(
                        new ProductCategory(1L, "sahumerios", "Sahumerios"),
                        new ProductCategory(2L, "perfumes", "Perfumes")
                ));

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slug").value("sahumerios"))
                .andExpect(jsonPath("$[1].slug").value("perfumes"));
    }
}
