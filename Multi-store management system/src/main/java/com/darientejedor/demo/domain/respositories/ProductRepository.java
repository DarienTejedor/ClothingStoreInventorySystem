package com.darientejedor.demo.domain.respositories;

import com.darientejedor.demo.domain.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

}
