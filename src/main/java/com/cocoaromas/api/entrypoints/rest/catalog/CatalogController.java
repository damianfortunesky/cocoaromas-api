package com.cocoaromas.api.entrypoints.rest.catalog;

import com.cocoaromas.api.application.port.in.catalog.GetPublicCategoriesUseCase;
import com.cocoaromas.api.application.port.in.catalog.GetPublicProductsUseCase;
import com.cocoaromas.api.domain.catalog.CatalogSortDirection;
import com.cocoaromas.api.domain.catalog.CatalogSortField;
import com.cocoaromas.api.domain.catalog.ProductCatalogQuery;
import com.cocoaromas.api.entrypoints.rest.catalog.CatalogDtos.CategoryResponse;
import com.cocoaromas.api.entrypoints.rest.catalog.CatalogDtos.ProductsPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Catalog", description = "Catálogo público de productos")
public class CatalogController {

    private final GetPublicProductsUseCase getPublicProductsUseCase;
    private final GetPublicCategoriesUseCase getPublicCategoriesUseCase;

    public CatalogController(
            GetPublicProductsUseCase getPublicProductsUseCase,
            GetPublicCategoriesUseCase getPublicCategoriesUseCase
    ) {
        this.getPublicProductsUseCase = getPublicProductsUseCase;
        this.getPublicCategoriesUseCase = getPublicCategoriesUseCase;
    }

    @GetMapping("/products")
    @Operation(summary = "Lista productos públicos", description = "Soporta búsqueda, filtro por categoría, orden y paginación" , security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido", content = @Content(schema = @Schema(implementation = ProductsPageResponse.class)))
    })
    public ProductsPageResponse getProducts(
            @Parameter(description = "Búsqueda simple por nombre")
            @RequestParam(required = false) String search,
            @Parameter(description = "Slug de categoría, ej: sahumerios")
            @RequestParam(required = false) String category,
            @Parameter(description = "Campo y dirección. Formatos: name,asc | name,desc | price,asc | price,desc")
            @RequestParam(required = false, defaultValue = "name,asc") String sort,
            @Parameter(description = "Página base cero")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(required = false, defaultValue = "12") int size
    ) {
        SortDefinition sortDefinition = SortDefinition.parse(sort);
        ProductCatalogQuery query = new ProductCatalogQuery(
                search,
                category,
                sortDefinition.field(),
                sortDefinition.direction(),
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 100)
        );

        return ProductsPageResponse.fromDomain(getPublicProductsUseCase.getProducts(query));
    }

    @GetMapping("/categories")
    @Operation(summary = "Lista categorías públicas" , security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))))
    })
    public List<CategoryResponse> getCategories() {
        return getPublicCategoriesUseCase.getCategories().stream()
                .map(CategoryResponse::fromDomain)
                .toList();
    }

    private record SortDefinition(CatalogSortField field, CatalogSortDirection direction) {
        static SortDefinition parse(String rawSort) {
            if (rawSort == null || rawSort.isBlank()) {
                return new SortDefinition(CatalogSortField.NAME, CatalogSortDirection.ASC);
            }

            String[] parts = rawSort.trim().toLowerCase().split(",");
            CatalogSortField field = switch (parts[0]) {
                case "price" -> CatalogSortField.PRICE;
                case "name" -> CatalogSortField.NAME;
                default -> CatalogSortField.NAME;
            };

            CatalogSortDirection direction = parts.length > 1 && "desc".equals(parts[1])
                    ? CatalogSortDirection.DESC
                    : CatalogSortDirection.ASC;

            return new SortDefinition(field, direction);
        }
    }
}
