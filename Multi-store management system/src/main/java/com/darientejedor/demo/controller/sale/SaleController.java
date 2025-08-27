package com.darientejedor.demo.controller.sale;


import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import com.darientejedor.demo.services.sale.ISaleService;
import com.darientejedor.demo.services.sale.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/sales")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Sales", description = "Endpoints for managing sales in the system.")
public class SaleController {


    private final ISaleService saleService;

    public SaleController(ISaleService saleService) {
        this.saleService = saleService;
    }

    /// GET
    @Operation(
            summary = "Lista all active sales.",
            description = "Returns a paginated list of all active sales in the system. The response depends on the user's role and store: " +
                    "'GENERAL_ADMIN' can view all sales " +
                    "'STORE_ADMIN' can only view sales from their own store" +
                    "'CASHIER' can only view sales from their own store",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not active sales found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER')")
    public ResponseEntity<Page<SaleResponse>> saleList(@PageableDefault(size = 10)Pageable pageable, Authentication authentication){
        return ResponseEntity.ok(saleService.listActiveSales(authentication ,pageable));
    }

    /// GET ID
    @Operation(
            summary = "Get a sale by id.",
            description = "Returns a sale by id if it's active. The response depends on the user's role and store: " +
                    "'GENERAL_ADMIN' can view all sales " +
                    "'STORE_ADMIN' can only view sales from their own store" +
                    "'CASHIER' can only view sales from their own store",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SaleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sale not found or is inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER')")
    public ResponseEntity<SaleResponse> saleResponse(@PathVariable Long id, Authentication authentication){
        return ResponseEntity.ok(saleService.saleResponse(id, authentication));
    }

    /// POST
    @Operation(
            summary = "Create a new sale",
            description = "Creates a new sale and returns the created sale. but only by CASHIER",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Sale created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SaleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<SaleResponse> createSale(@RequestBody @Valid SaleData saleData){
        SaleResponse sale = saleService.createSale(saleData);
        URI ubication = URI.create("/sales/" + sale.id());
        return ResponseEntity.created(ubication).body(sale);
    }

    /// DELETE
    @Operation(
            summary = "Deactivate a sale.",
            description = "Deactivates an existing sale by its ID. But only by GENERAL_ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Sale deactivated successfully.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sale not found with the provided ID.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The sale is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('GENERAL_ADMIN')")
    public ResponseEntity<Void> deactiveSale(@PathVariable Long id){
        saleService.deactiveSale(id);
        return ResponseEntity.noContent().build();
    }
}


















