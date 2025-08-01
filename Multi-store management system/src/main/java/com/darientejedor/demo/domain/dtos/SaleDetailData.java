package com.darientejedor.demo.domain.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SaleDetailData(
        @NotNull
        Long saleId,
        @NotNull
        Long productId,
        @NotNull
        BigDecimal unitPrice,
        @NotNull
        Long quantity
) {
}
