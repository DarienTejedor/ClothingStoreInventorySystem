package com.darientejedor.demo.domain.salesdetails.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaleDetailData(
        @NotNull
        @Positive
        Long productId,
        @NotNull
        @Positive
        Long quantity
) {
}
