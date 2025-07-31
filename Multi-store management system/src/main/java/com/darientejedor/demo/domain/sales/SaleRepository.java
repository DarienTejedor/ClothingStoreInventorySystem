package com.darientejedor.demo.domain.sales;

import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Sale> findByUser(User user);

    List<Sale> findByStore(Store store);
}
