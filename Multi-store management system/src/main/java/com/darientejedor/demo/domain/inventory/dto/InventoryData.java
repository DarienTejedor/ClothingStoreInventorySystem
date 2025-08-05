package com.darientejedor.demo.domain.inventory.dto;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.products.Product;
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
