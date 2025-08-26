package com.darientejedor.demo.controller.inventory;


import com.darientejedor.demo.domain.inventory.dto.InventoryData;
import com.darientejedor.demo.domain.inventory.dto.InventoryResponse;
import com.darientejedor.demo.domain.inventory.dto.InventoryUpdateData;
import com.darientejedor.demo.services.inventory.InventoryService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/inventory")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Inventory", description = "Endpoints for managing inventories in the system.")
public class InventoryController {


    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(
            summary = "List all active inventories.",
            description = "Returns a paginated list of all active inventories in the system.",
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
                            description = "No active inventories found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER')")
    public ResponseEntity<Page<InventoryResponse>> inventoryList(@PageableDefault(size = 20) Pageable pageable, Authentication authentication){
        return ResponseEntity.ok(inventoryService.listActiveInventories(authentication, pageable));
    }

    @Operation(
            summary = "Get an inventory by ID.",
            description = "Returns an inventory by its ID if it is active.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inventory not found or is inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> inventoryResponse(@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.inventoryResponse(id));
    }

    @Operation(
            summary = "Get inventories by product name.",
            description = "Returns a paginated list of inventories for a specific product name if they are active.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inventories not found or are inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/product/by-name/{name}")
    public ResponseEntity<Page<InventoryResponse>> inventoryPerProductName(@PageableDefault(size = 10) Pageable pageable, @PathVariable("name") String productName){
        return ResponseEntity.ok(inventoryService.inventoryPerProductName(pageable, productName));
    }

    @Operation(
            summary = "Get inventories by store ID.",
            description = "Returns a paginated list of inventories for a specific store ID if they are active.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inventories not found or are inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/store/by-id/{id}")
    public ResponseEntity<Page<InventoryResponse>> inventoryPerStore(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long id){
        return ResponseEntity.ok(inventoryService.inventoryPerStore(id, pageable));
    }

    @Operation(
            summary = "Get inventories by product ID.",
            description = "Returns a paginated list of inventories for a specific product ID if they are active.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inventories not found or are inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/product/by-id/{id}")
    public ResponseEntity<Page<InventoryResponse>> inventoryPerProductId(@PageableDefault(size = 10) Pageable pageable,@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.inventoryPerProduct(id, pageable));
    }

    @Operation(
            summary = "Create or update an inventory.",
            description = "Creates a new inventory or updates an existing one if it already exists, and returns its details.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Inventory created or updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or a required entity (product/store) is missing.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody @Valid InventoryData inventoryData){
        InventoryResponse inventory = inventoryService.createOrUpdateInventory(inventoryData);
        URI ubication = URI.create("/inventory/" + inventory.id());
        return ResponseEntity.created(ubication).body(inventory);
    }

    @Operation(
            summary = "Update an inventory's stock.",
            description = "Updates an existing inventory's stock with the provided data and returns the updated inventory.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inventory stock updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inventory not found with the provided IDs.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or the inventory is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{productId}/{storeId}")
    @Transactional
    public ResponseEntity<InventoryResponse> updateStock(@PathVariable Long productId, @PathVariable Long storeId, @RequestBody @Valid InventoryUpdateData inventoryUpdateData){
        InventoryResponse inventoryResponse = inventoryService.updateStock(productId, storeId, inventoryUpdateData);
        return ResponseEntity.ok(inventoryResponse);
    }

    @Operation(
            summary = "Deactivate an inventory.",
            description = "Deactivates an existing inventory by its product and store IDs.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Inventory deactivated successfully.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inventory not found with the provided IDs.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The inventory is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{productId}/{storeId}")
    @Transactional
    public ResponseEntity<Void> deleteInventory(@PathVariable Long productId, @PathVariable Long storeId){
        inventoryService.deactiveInventory(productId,storeId);
        return ResponseEntity.noContent().build();
    }
}