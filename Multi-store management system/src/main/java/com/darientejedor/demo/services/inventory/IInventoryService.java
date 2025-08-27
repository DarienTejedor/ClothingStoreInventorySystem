package com.darientejedor.demo.services.inventory;

import com.darientejedor.demo.domain.inventory.dto.InventoryData;
import com.darientejedor.demo.domain.inventory.dto.InventoryResponse;
import com.darientejedor.demo.domain.inventory.dto.InventoryUpdateData;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface IInventoryService {

    Page<InventoryResponse> listActiveInventories(Authentication authentication, Pageable pageable);

    Page<InventoryResponse> inventoryPerProductName(Pageable pageable, String productName);

    Page<InventoryResponse> inventoryPerStore(Long id, Pageable pageable);

    Page<InventoryResponse> inventoryPerProduct(Long id, Pageable pageable);

    InventoryResponse inventoryResponse(Long id);

    InventoryResponse createOrUpdateInventory(@Valid InventoryData inventoryData);

    InventoryResponse updateStock(Long productId, Long storeId, @Valid InventoryUpdateData inventoryUpdateData);

    void deactiveInventory(Long productId, Long storeId);


}
