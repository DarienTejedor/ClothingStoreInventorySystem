package com.darientejedor.demo.domain.users.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateRoleAndStoreData(
        @NotNull
        Long roleId,
        @NotNull
        Long storeId
) {
}
