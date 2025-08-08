package com.darientejedor.demo.domain.sales.dto;

import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public record SaleResponse(
        Long id,
        LocalDateTime saleDate,
        BigDecimal totalSale,
        Long storeId,
        Long userId,
        List<SaleDetailResponse> saleDetails
) {
    public SaleResponse(Sale sale){
       this(sale.getId(),
        sale.getSaleDate(),
        sale.getTotalSale(),
        sale.getStore().getId(),
        sale.getUser().getId(),
               sale.getSaleDetails().stream()
                       .map(SaleDetailResponse::new)
                       .collect(Collectors.toList()));
    }

}
