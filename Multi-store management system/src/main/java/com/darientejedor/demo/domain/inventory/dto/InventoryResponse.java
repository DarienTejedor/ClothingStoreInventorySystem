package com.darientejedor.demo.domain.inventory.dto;

import com.darientejedor.demo.domain.inventory.Inventory;

public record InventoryResponse(
        Long id,
        Long stock,
        Long productId,
        String productName,
        Long storeId,
        String storeName
) {
    public InventoryResponse(Inventory inventory){
        this(
                inventory.getId(),
                inventory.getStock(),
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                inventory.getStore().getId(),
                inventory.getStore().getName()
        );
    }

}
