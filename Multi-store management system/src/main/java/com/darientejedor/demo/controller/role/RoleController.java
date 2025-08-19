package com.darientejedor.demo.controller.role;

import com.darientejedor.demo.domain.roles.dto.RoleData;
import com.darientejedor.demo.domain.roles.dto.RoleResponse;
import com.darientejedor.demo.services.role.IRoleService;
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
@RequestMapping("/roles")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Roles", description = "Endpoints for managing roles in the system.")
public class RoleController {


    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(
            summary = "List all active roles.",
            description = "Returns a paginated list of all active roles in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not active roles found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<RoleResponse>> rolesList(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(roleService.listActiveRoles(pageable));
    }

    @Operation(
            summary = "Get a role by ID.",
            description = "Returns a role by id if it's active .",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Role not found or is inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> roleResponse(@PathVariable Long id){
        return ResponseEntity.ok(roleService.roleResponse(id));
    }

    @Operation(
            summary = "Create a new role",
            description = "Creates a new role and returns the created role.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Role created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or a role with this name already exists.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid RoleData roleData){
        RoleResponse role = roleService.createRole(roleData);
        URI ubication = URI.create("/roles/" + role.id());
        return ResponseEntity.created(ubication).body(role);
    }

    @Operation(
            summary = "Update a role",
            description = "Update a role and returns its response.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Role not found with the provided ID",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or the role is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @RequestBody @Valid RoleData roleData){
        RoleResponse roleResponse = roleService.updateRole(id, roleData);
        return ResponseEntity.ok(roleResponse);

    }

    @Operation(
            summary = "Deactivate a role.",
            description = "Deactivates an existing role by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Role deactivated successfully.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Role not found with the provided ID.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The role is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deactiveRole(id);
        return ResponseEntity.noContent().build();
    }
}






