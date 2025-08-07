package com.darientejedor.demo.domain.sales.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleData(
        @NotNull
        Long storeId,
        @NotNull
        Long userId
) {
}

