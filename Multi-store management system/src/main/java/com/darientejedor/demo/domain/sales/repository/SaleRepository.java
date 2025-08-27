package com.darientejedor.demo.domain.sales.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Sale> findByUser(User user);

    List<Sale> findByStore(Store store);

    Page<Sale> findByActiveTrue(Pageable pageable);

    Page<Sale> findByStoreId(Long storeId, Pageable pageable);
}
