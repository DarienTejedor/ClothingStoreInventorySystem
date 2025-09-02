package com.darientejedor.demo.services.inventory;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.inventory.dto.InventoryData;
import com.darientejedor.demo.domain.inventory.dto.InventoryResponse;
import com.darientejedor.demo.domain.inventory.dto.InventoryUpdateData;
import com.darientejedor.demo.domain.inventory.repository.InventoryRepository;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.services.inventory.validations.IIventoryValidations;
import com.darientejedor.demo.services.product.IProductService;
import com.darientejedor.demo.services.product.validations.IProductValidations;
import com.darientejedor.demo.services.store.validations.IStoreValidations;
import com.darientejedor.demo.services.user.authentications.IUserAuthentications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService implements IInventoryService{

    private final InventoryRepository inventoryRepository;
    private final IProductValidations productValidations;
    private final IUserAuthentications userAuthentications;
    private final IStoreValidations storeValidations;
    private final IIventoryValidations inventoryValidations;

    public InventoryService(InventoryRepository inventoryRepository,
                            IProductValidations productValidations,
                            IUserAuthentications userAuthentications,
                            IStoreValidations storeValidations,
                            IIventoryValidations inventoryValidations) {
        this.inventoryRepository = inventoryRepository;
        this.productValidations = productValidations;
        this.userAuthentications = userAuthentications;
        this.storeValidations = storeValidations;
        this.inventoryValidations = inventoryValidations;
    }

    @Override
    public Page<InventoryResponse> listActiveInventories(Authentication authentication, Pageable pageable) {
        User authUser = userAuthentications.authUser(authentication);
        String role = userAuthentications.authRole(authentication);
        if ("ROLE_GENERAL_ADMIN".equals(role)) {
            return inventoryRepository.findByActiveTrue(pageable).map(InventoryResponse::new);
        } else {
            Long storeId = authUser.getStore().getId();
            return inventoryRepository.findByStoreId(storeId, pageable).map(InventoryResponse::new);
        }
    }

    @Override
    public Page<InventoryResponse> inventoryByProductName(Pageable pageable, String productName) {
        return inventoryRepository.findByProduct_NameContainingIgnoreCase(productName, pageable).map(InventoryResponse::new);
    }

    @Override
    public Page<InventoryResponse> inventoryByStore(Long id, Pageable pageable) {
        return inventoryRepository.findByStoreId(id, pageable).map(InventoryResponse::new);
    }

    @Override
    public Page<InventoryResponse> inventoryByProduct(Long id, Pageable pageable) {
        return inventoryRepository.findByProductId(id, pageable).map(InventoryResponse::new);
    }

    @Override
    public InventoryResponse inventoryResponse(Long id, Authentication authentication) {
        Inventory inventory = inventoryValidations.validInventory(id);
        User authUser = userAuthentications.authUser(authentication);
        String role = userAuthentications.authRole(authentication);
        switch (role){
            case "ROLE_GENERAL_ADMIN":
                break;
            case "ROLE_STORE_ADMIN", "ROLE_CASHIER":
                if (!authUser.getStore().getId().equals(inventory.getStore().getId())){
                    throw new AccessDeniedException("You can only view inventories from your own store");
                }
                break;
            default:
                throw new ValidationException("Invalid user role: " + role);
        }
        return new InventoryResponse(inventory);
    }

    @Override
    public InventoryResponse createOrUpdateInventory(@Valid InventoryData inventoryData) {
        //Busca las entidades Product y Store po Id
        var validated = validateActiveProductAndStore(inventoryData.productId(), inventoryData.storeId());
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
                .orElseThrow(()-> new ValidationException("Inventory not found"));

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
                .orElseThrow(() -> new ValidationException("Inventory not found"));
        //Desactivar y guardar los cambios
        inventory.deactiveInventory();
        inventoryRepository.save(inventory);

    }

    //VAlIDADOR de tienda y producto existenten y activos
    private ProductAndStore validateActiveProductAndStore(Long productId, Long storeId) {
        // Valida que el producto exista y esté activo
        Product product = productValidations.validProduct(productId);
        // Valida que la tienda exista y esté activa
        Store store = storeValidations.validStore(storeId);
        return new ProductAndStore(product, store);
    }

    private record ProductAndStore(Product product, Store store) {}
}

