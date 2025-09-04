package com.darientejedor.demo.domain.sales.dto;

import jakarta.validation.constraints.NotNull;


public record SaleData(
        @NotNull
        Long storeId,
        @NotNull
        Long userId
) {
}

