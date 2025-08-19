package com.darientejedor.demo.controller.store;


import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import com.darientejedor.demo.domain.stores.dto.StoreData;
import com.darientejedor.demo.domain.stores.dto.StoreResponse;
import com.darientejedor.demo.services.store.StoreService;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/stores")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Stores", description = "Endpoints for managing stores in the system.")
public class StoreController {



    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(
            summary = "List all active stores.",
            description = "Returns a paginated list of all active stores in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not active stores found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    private ResponseEntity<Page<StoreResponse>> storeList(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(storeService.listActiveStores(pageable));
    }

    @Operation(
            summary = "Get a store by ID.",
            description = "Returns a store by id if it's active .",
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
    private ResponseEntity<StoreResponse> storeResponse(@PathVariable Long id){
        return ResponseEntity.ok(storeService.storeResponse(id));
    }

    @Operation(
            summary = "Create a new store",
            description = "Creates a new store and returns the created store.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Store created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or a Store with this name already exists.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@RequestBody @Valid StoreData storeData){
        StoreResponse store = storeService.createStore(storeData);
        URI ubication = URI.create("/stores/" + store.id());
        return ResponseEntity.created(ubication).body(store);
    }

    @Operation(
            summary = "Update a store",
            description = "Updates an existing store and returns the details of the updated store.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "store updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponse.class))
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
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long id, @RequestBody @Valid StoreData storeData){
        StoreResponse storeResponse = storeService.updateStore(id, storeData);
        return ResponseEntity.ok(storeResponse);
    }

    @Operation(
            summary = "Deactivate a Store.",
            description = "Deactivates an existing store by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Store deactivated successfully.",
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
    public ResponseEntity<Void> deleteStore(@PathVariable Long id){
        storeService.deactiveStore(id);
        return ResponseEntity.noContent().build();
    }
}
