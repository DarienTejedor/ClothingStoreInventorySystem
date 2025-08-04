package com.darientejedor.demo.domain.products.dtos;

import com.darientejedor.demo.domain.products.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price
) {
    public ProductResponse(Product product){
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
