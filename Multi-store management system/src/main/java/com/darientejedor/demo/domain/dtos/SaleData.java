package com.darientejedor.demo.domain.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleData(
        @NotNull
        LocalDateTime saleDate,
        @NotNull
        BigDecimal totalSale,
        @NotNull
        Long storeId,
        @NotNull
        Long userId
) {
}

