package com.darientejedor.demo.domain.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductData(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        BigDecimal price
) {
}
