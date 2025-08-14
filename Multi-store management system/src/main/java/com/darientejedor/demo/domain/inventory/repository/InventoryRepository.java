package com.darientejedor.demo.domain.inventory.repository;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.stores.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductAndStore(Product product, Store store);

    Page<Inventory> findByActiveTrue(Pageable pageable);

    Optional<Inventory> findByProductAndStoreAndActiveTrue(Product product, Store store);

    Page<Inventory> findByStoreId(Long id, Pageable pageable);

    Page<Inventory> findByProductId(Long id, Pageable pageable);

    Page<Inventory> findByProduct_NameContainingIgnoreCase(String productName, Pageable pageable);
}
