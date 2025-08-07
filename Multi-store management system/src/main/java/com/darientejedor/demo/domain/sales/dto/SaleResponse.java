package com.darientejedor.demo.domain.sales.dto;

import com.darientejedor.demo.domain.sales.Sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponse(
        Long id,
        LocalDateTime saleDate,
        BigDecimal totalSale,
        Long storeId,
        Long userId
) {
    public SaleResponse(Sale sale){
       this(sale.getId(),
        sale.getSaleDate(),
        sale.getTotalSale(),
        sale.getStore().getId(),
        sale.getUser().getId());
    }
}
