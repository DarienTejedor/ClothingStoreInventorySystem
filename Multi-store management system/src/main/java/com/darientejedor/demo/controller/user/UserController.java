package com.darientejedor.demo.controller.user;

import com.darientejedor.demo.domain.users.dto.*;
import com.darientejedor.demo.services.user.IUserService;
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
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Users", description = "Endpoints for managing users in the system.")
public class UserController {


    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "List users based on authentication role and store ID.",
            description = "Returns a paginated list of active users. The list depends on the user's role: " +
                    "• 'GENERAL_ADMIN' can get a list of all users or filter by storeId. " +
                    "• 'STORE_ADMIN' can only get users from their own store. " +
                    "The storeId parameter is optional and only applies to 'ADMIN_GENERAL'.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation. Returns a paginated list of users.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The authenticated user does not have the required permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<Page<UserResponse>> userList(
                Authentication authentication,
                @RequestParam (required = false) Long storeId,
                @PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(userService.listActiveUsers(authentication, storeId, pageable));
    }


    @Operation(
            summary = "Get a user by ID based on authentication role.",
            description = "Returns a user by id if it's active, The responser depends on the user's role:" +
                    "'GENERAL_ADMIN' can get any user." +
                    "'STORE_ADMIN' can only get users from their own store." +
                    "'CASHIER' can only get their own user profile",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found or is inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER')")
    public ResponseEntity<UserResponse> userResponse (@PathVariable  Long id, Authentication authentication){
        return ResponseEntity.ok(userService.userResponse(id, authentication));
    }

    @Operation(
            summary = "Create a new user based on authentication role",
            description = "Creates a new user and returns the created user, the responser depend on the user's role:" +
                    "'GENERAL_ADMIN' can create any user." +
                    "'STORE_ADMIN' can only create users from their own store",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or a user with this name already exists.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserData userData, Authentication authentication){
        UserResponse user = userService.createUser(userData, authentication);
        URI uri = URI.create("/users/" + user.id());
        return ResponseEntity.created(uri).body(user);
    }

    @Operation(
            summary = "Update a user information based on authentication role",
            description = "Updates a user's information (except password, role, and store) and returns the updated user. the responser depend on the user's role:" +
                    "'GENERAL_ADMIN' can update any user" +
                    "'STORE_ADMIN' can only update users from their own store",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found with the provided ID",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or the user is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserInformation userInformation,
                                                   @PathVariable Long id,
                                                   Authentication authentication){
        UserResponse userResponse = userService.updateUserInfo(id, userInformation, authentication);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Update a user password based on authentication",
            description = "Update a user password and returns its response.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found with the provided ID",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or the user is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{id}/password")
    @Transactional
    @PreAuthorize("hasAnyRole('GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER')")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable Long id,
                                                       @RequestBody @Valid PasswordUpdateData updatePassword,
                                                       Authentication authentication){
        UserResponse userResponse = userService.changePassword(id, updatePassword, authentication);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Update a user role and store",
            description = "Updates a user's role and store. but only by GENERAL_ADMIN",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found with the provided ID",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or the user is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{id}/role-store")
    @Transactional
    @PreAuthorize("hasRole('GENERAL_ADMIN')")
    public ResponseEntity<UserResponse> updateRoleAndStore(@PathVariable Long id,
                                                           @RequestBody @Valid UpdateRoleAndStoreData updateRoleAndStore){
        UserResponse userResponse = userService.updateRoleAndStore(id, updateRoleAndStore);
        return  ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Deactivate a user.",
            description = "Deactivates an existing user by its ID. " +
                    "'GENERAL_ADMIN' can deactivate any user." +
                    "'STORE_ADMIN' can only deactivated users from their own store",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "User deactivated successfully.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied. User doesn't have permissions.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found with the provided ID.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user is already inactive.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('GENERAL_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           Authentication authentication){
        userService.deactiveUser(id, authentication);
        return ResponseEntity.noContent().build();
    }
}

