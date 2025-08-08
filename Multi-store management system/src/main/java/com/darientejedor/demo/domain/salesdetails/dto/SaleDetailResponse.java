package com.darientejedor.demo.domain.salesdetails.dto;

import com.darientejedor.demo.domain.salesdetails.SaleDetail;

import java.math.BigDecimal;

public record SaleDetailResponse(
        Long id,
        Long saleId,
        Long productId,
        Long quantity,
        BigDecimal unitPrice
) {
    public SaleDetailResponse(SaleDetail saleDetail) {
        this(
                saleDetail.getId(),
                saleDetail.getSale().getId(),
                saleDetail.getProduct().getId(),
                saleDetail.getQuantity(),
                saleDetail.getUnitPrice()
        );
    }
}
