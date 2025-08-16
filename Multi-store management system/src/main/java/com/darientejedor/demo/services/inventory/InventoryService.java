package com.darientejedor.demo.services.inventory;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.inventory.dto.InventoryData;
import com.darientejedor.demo.domain.inventory.dto.InventoryResponse;
import com.darientejedor.demo.domain.inventory.dto.InventoryUpdateData;
import com.darientejedor.demo.domain.inventory.repository.InventoryRepository;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.products.repository.ProductRepository;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.stores.repository.StoreRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService implements IInventoryService{

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Page<InventoryResponse> listActiveInventories(Pageable pageable) {
        return inventoryRepository.findByActiveTrue(pageable).map(InventoryResponse::new);
    }

    @Override
    public Page<InventoryResponse> inventoryPerProductName(Pageable pageable, String productName) {
        return inventoryRepository.findByProduct_NameContainingIgnoreCase(productName, pageable).map(InventoryResponse::new);
    }

    @Override
    public Page<InventoryResponse> inventoryPerStore(Long id, Pageable pageable) {
        return inventoryRepository.findByStoreId(id, pageable).map(InventoryResponse::new);
    }

    @Override
    public Page<InventoryResponse> inventoryPerProduct(Long id, Pageable pageable) {
        return inventoryRepository.findByProductId(id, pageable).map(InventoryResponse::new);
    }

    @Override
    public InventoryResponse inventoryResponse(Long id) {
            Inventory inventory = inventoryRepository.findById(id)
                    .orElseThrow(()-> new IllegalArgumentException("Inventory not found with ID: " + id));
            if (!inventory.isActive()){
                throw new IllegalArgumentException("Inventory not found or inactive with ID: " + id);
            }
            return new InventoryResponse(
                    inventory.getId(),
                    inventory.getStock(),
                    inventory.getProduct().getId(),
                    inventory.getProduct().getName(),
                    inventory.getStore().getId(),
                    inventory.getStore().getName()
            );
    }

    @Override
    public InventoryResponse createOrUpdateInventory(@Valid InventoryData inventoryData) {
        //Busca las entidades Product y Store po Id
        ProductAndStore validated = validateActiveProductAndStore(inventoryData.productId(), inventoryData.storeId());
        //Busca y valida si ya existe un inventario de ese producto en esa tienda
        Optional<Inventory> existingInventory = inventoryRepository.findByProductAndStore(validated.product, validated.store);
        Inventory inventory;
        if (existingInventory.isPresent()) {

            //Si existe actualiza stok
            inventory = existingInventory.get();
            inventory.setStock(inventoryData.stock());
        } else {
            //sino crea un nuevo inventario
            inventory = new Inventory(validated.product, validated.store, inventoryData.stock());
        }
        inventoryRepository.save(inventory);
        return new InventoryResponse(inventory);
    }

    @Override
    public InventoryResponse updateStock(Long productId, Long storeId, @Valid InventoryUpdateData inventoryUpdateData) {
        //Valida que el producto y tienda existan y estén activos
        ProductAndStore validated = validateActiveProductAndStore(productId, storeId);

        Inventory inventory = inventoryRepository.findByProductAndStoreAndActiveTrue(validated.product, validated.store)
                .orElseThrow(()-> new IllegalArgumentException("Inventory not found"));

        inventory.setStock(inventoryUpdateData.stock());
        inventoryRepository.save(inventory);

        return new InventoryResponse(inventory);
    }

    @Override
    public void deactiveInventory(Long productId, Long storeId) {
        //Valida que el producto y tienda existan y estén activos
        ProductAndStore validated = validateActiveProductAndStore(productId, storeId);
        //Buscar el inventario por la clave compuesta
        Inventory inventory = inventoryRepository.findByProductAndStoreAndActiveTrue(validated.product,validated.store)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        //Desactivar y guardar los cambios
        inventory.deactiveInventory();
        inventoryRepository.save(inventory);

    }

    //VAlIDADOR de tienda y producto existente y active
    private ProductAndStore validateActiveProductAndStore(Long productId, Long storeId) {
        // Valida que el producto exista y esté active
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        if (!product.isActive()){
            throw new IllegalArgumentException("Product not found or already inactive with ID: " + productId);
        }

        // Valida que la tienda exista y esté activa
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
        if (!store.isActive()){
            throw new IllegalArgumentException("Store not found or already inactive with ID: " + storeId);
        }

        return new ProductAndStore(product, store);
    }



    private record ProductAndStore(Product product, Store store) {}
}

