package com.darientejedor.demo.domain.inventory.dto;

import com.darientejedor.demo.domain.inventory.Inventory;

public record InventoryResponse(
        Long id,
        Long stock,
        Long productId,
        Long storeId
) {
    public InventoryResponse(Inventory inventory){
        this(
                inventory.getId(),
                inventory.getStock(),
                inventory.getProduct().getId(),
                inventory.getStore().getId());
    }

}
