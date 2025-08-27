package com.darientejedor.demo.controller.product;

import com.darientejedor.demo.domain.products.dtos.ProductData;
import com.darientejedor.demo.domain.products.dtos.ProductResponse;
import com.darientejedor.demo.services.product.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("products")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Products", description = "Endpoints for managing products in the system.")
public class ProductController {


    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "List all active products.",
            description = "Returns a paginated list of all active products in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not active products found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    public ResponseEntity<Page<ProductResponse>> productList(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(productService.listActiveProducts(pageable));
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product and returns the created product.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or a product with this name already exists.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductData productData){
        ProductResponse product = productService.createProduct(productData);
        URI ubication = URI.create("/products/" + product.id());
        return ResponseEntity.created(ubication).body(product);
    }

    @Operation(
            summary = "Update a product",
            description = "Update a product and returns its response.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found with the provided ID",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or the product is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductData productData){
        ProductResponse productResponse = productService.updateProduct(id, productData);
        return ResponseEntity.ok(productResponse);
    }

    @Operation(
            summary = "Deactivate a product.",
            description = "Deactivates an existing product by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product deactivated successfully.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found with the provided ID.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The product is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deactiveProduct(id);
        return ResponseEntity.noContent().build();
    }
}
