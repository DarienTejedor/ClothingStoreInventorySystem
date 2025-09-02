package com.darientejedor.demo.controller.store;


import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import com.darientejedor.demo.services.store.IStoreService;
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
@RequestMapping("/stores")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Stores", description = "Endpoints for managing stores in the system.")
public class StoreController {



    private final IStoreService storeService;

    public StoreController(IStoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(
            summary = "List all active stores based on authentication role.",
            description = "Returns a paginated list of all active stores in the system. but only by GENERAL_ADMIN",
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
                            description = "Not active stores found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    private ResponseEntity<Page<StoreResponse>> storeList(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(storeService.listActiveStores(pageable));
    }

    @Operation(
            summary = "Get a store by ID, authentication role and store ID.",
            description = "Returns a store by id if it's active. The response depends on the user's role and store: " +
                    "'GENERAL_ADMIN' can view all stores" +
                    "'STORE_ADMIN' can only view their own store" +
                    "'CASHIER' can only view their own store",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Store not found or is inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER')")
    private ResponseEntity<StoreResponse> storeResponse(@PathVariable Long id, Authentication authentication){
        return ResponseEntity.ok(storeService.storeResponse(id, authentication));
    }

    @Operation(
            summary = "Create a new store",
            description = "Creates a new store and returns the created store. but only by GENERAL_ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Store created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponse.class))
                    ),

                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or a Store with this name already exists.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    public ResponseEntity<StoreResponse> createStore(@RequestBody @Valid StoreData storeData){
        StoreResponse store = storeService.createStore(storeData);
        URI ubication = URI.create("/stores/" + store.id());
        return ResponseEntity.created(ubication).body(store);
    }

    @Operation(
            summary = "Update a store",
            description = "Updates an existing store and returns the details of the updated store. but only by GENERAL_ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "store updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponse.class))
                    ),

                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Store not found with the provided ID",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or the store is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long id, @RequestBody @Valid StoreData storeData){
        StoreResponse storeResponse = storeService.updateStore(id, storeData);
        return ResponseEntity.ok(storeResponse);
    }

    @Operation(
            summary = "Deactivate a Store.",
            description = "Deactivates an existing store by its ID. but only by GENERAL_ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Store deactivated successfully.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),

                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Store not found with the provided ID.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The Store is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN')")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id){
        storeService.deactiveStore(id);
        return ResponseEntity.noContent().build();
    }
}
