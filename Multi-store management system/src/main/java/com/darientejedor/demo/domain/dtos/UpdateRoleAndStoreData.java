package com.darientejedor.demo.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleAndStoreData(
        @NotNull
        Long roleId,
        @NotNull
        Long storeId
) {
}
