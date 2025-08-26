package com.darientejedor.demo.services.inventory;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.inventory.dto.InventoryData;
import com.darientejedor.demo.domain.inventory.dto.InventoryResponse;
import com.darientejedor.demo.domain.inventory.dto.InventoryUpdateData;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface IInventoryService {

    public Page<InventoryResponse> listActiveInventories(Authentication authentication, Pageable pageable);

    public Page<InventoryResponse> inventoryPerProductName(Pageable pageable, String productName);

    public Page<InventoryResponse> inventoryPerStore(Long id, Pageable pageable);

    public Page<InventoryResponse> inventoryPerProduct(Long id, Pageable pageable);

    public InventoryResponse inventoryResponse(Long id);

    public InventoryResponse createOrUpdateInventory(@Valid InventoryData inventoryData);

    public InventoryResponse updateStock(Long productId, Long storeId, @Valid InventoryUpdateData inventoryUpdateData);

    public void deactiveInventory(Long productId, Long storeId);


}
