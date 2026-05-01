package com.darientejedor.demo.domain.stores.repository;

import com.darientejedor.demo.domain.stores.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>{

    Optional<Store> findByName(String name);

    @Query("SELECT s FROM Store s WHERE s.active = true AND (" +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.address.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.address.locality) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.address.street) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CAST(s.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Store> searchStores(@Param("searchTerm") String searchTerm, Pageable pageable);

    Optional<Store> findByEmail(String email);

    Page<Store> findByActiveTrue(Pageable pageable);
}
