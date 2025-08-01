package com.darientejedor.demo.domain.respositories;

import com.darientejedor.demo.domain.stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>{

    Optional<Store> findByName(String name);

    Optional<Store> findByEmail(String email);
}
