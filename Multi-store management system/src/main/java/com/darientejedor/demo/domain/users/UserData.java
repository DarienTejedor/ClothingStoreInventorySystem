package com.darientejedor.demo.domain.users;

import com.darientejedor.demo.domain.roles.Role;
import com.darientejedor.demo.domain.stores.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserData(
    @NotBlank
    String loginUser,
    @NotBlank
    String name,
    @NotBlank
    String password,
    @NotNull
    Long document,
    @NotNull
    Long roleId,
    @NotNull
    Long storeId
) {
}
