package com.darientejedor.demo.domain.inventory.dto;

import jakarta.validation.constraints.NotNull;

public record InventoryUpdateData(
        @NotNull
        Long stock
) {
}
