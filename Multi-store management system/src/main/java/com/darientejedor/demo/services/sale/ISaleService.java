package com.darientejedor.demo.services.sale;

import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISaleService {
    public Page<SaleResponse> listActiveSales(Pageable pageable);

    public SaleResponse saleResponse(Long id);

    public SaleResponse createSale(@Valid SaleData saleData);

    public void deactiveSale(Long id);

    public Sale validSale(Long id);
}
