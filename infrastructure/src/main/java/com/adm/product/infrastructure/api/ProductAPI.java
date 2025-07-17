package com.adm.product.infrastructure.api;

import com.adm.product.domain.pagination.Pagination;
import com.adm.product.infrastructure.product.models.CreateProductApiInput;
import com.adm.product.infrastructure.product.models.ProductApiOutPut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "products")
@Tag(name = "Product")
public interface ProductAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new product", description = "Create a new product with command")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable error"),
            @ApiResponse(responseCode = "500", description = "An internal server error")
    })
    ResponseEntity<?> createProduct(@RequestBody CreateProductApiInput input);

    @GetMapping()
    @Operation(summary = "List all product paginated", description = "Get a product with ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter"),
            @ApiResponse(responseCode = "500", description = "An internal server error")
    })
    Pagination<?> listProducts(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a product by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product was no found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ProductApiOutPut getById(@PathVariable(name = "id") String id);
}
