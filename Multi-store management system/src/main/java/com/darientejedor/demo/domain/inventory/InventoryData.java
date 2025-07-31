package com.darientejedor.demo.domain.inventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryData(
        @NotNull
        Long stock,
        @NotNull
        Long productId,
        @NotNull
        Long storeId
) {
}
