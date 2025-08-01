package com.darientejedor.demo.domain.dtos;

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
