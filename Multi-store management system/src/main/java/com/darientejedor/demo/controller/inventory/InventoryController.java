package com.darientejedor.demo.controller.inventory;


import com.darientejedor.demo.domain.inventory.dto.InventoryData;
import com.darientejedor.demo.domain.inventory.dto.InventoryResponse;
import com.darientejedor.demo.domain.inventory.dto.InventoryUpdateData;
import com.darientejedor.demo.services.inventory.InventoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {


    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<Page<InventoryResponse>> inventoryList(@PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(inventoryService.listActiveInventories(pageable).map(InventoryResponse::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> inventoryResponse(@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.inventoryResponse(id));
    }

    @GetMapping("/inventory/product/{name}")
    public ResponseEntity<Page<InventoryResponse>> inventoryPerProductName(@PageableDefault(size = 10) Pageable pageable, @PathVariable("name") String productName){
        return ResponseEntity.ok(inventoryService.inventoryPerProductName(pageable, productName).map(InventoryResponse::new));
    }

    @GetMapping("/inventory/store/{id}")
    public ResponseEntity<Page<InventoryResponse>> inventoryPerStore(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long id){
        return ResponseEntity.ok(inventoryService.inventoryPerStore(pageable, id).map(InventoryResponse::new));
    }

    @GetMapping("/inventory/product/{id}")
    public ResponseEntity<Page<InventoryResponse>> inventoryPerProductId(@PageableDefault(size = 10) Pageable pageable,@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.inventoryPerProduct(pageable, id).map(InventoryResponse::new));
    }


    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody @Valid InventoryData inventoryData){
        InventoryResponse inventoryResponse = inventoryService.createOrUpdateInventory(inventoryData);
        return ResponseEntity.ok(inventoryResponse);
    }

    @PutMapping("/{productId}/{storeId}")
    @Transactional
    public ResponseEntity<InventoryResponse> updateStock(@PathVariable Long productId, @PathVariable Long storeId, @RequestBody @Valid InventoryUpdateData inventoryUpdateData){
        InventoryResponse inventoryResponse = inventoryService.updateStock(productId, storeId, inventoryUpdateData);
        return ResponseEntity.ok(inventoryResponse);
    }


    @DeleteMapping("/{productId}/{storeId}")
    @Transactional
    public ResponseEntity<Void> deleteInventory(@PathVariable Long productId, @PathVariable Long storeId){
        inventoryService.deactiveInventory(productId,storeId);
        return ResponseEntity.noContent().build();
    }

}















