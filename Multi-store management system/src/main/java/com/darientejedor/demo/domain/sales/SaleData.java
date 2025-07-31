package com.darientejedor.demo.domain.sales;

import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

