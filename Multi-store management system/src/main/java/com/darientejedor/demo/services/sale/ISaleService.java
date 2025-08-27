package com.darientejedor.demo.services.sale;

import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ISaleService {
    public Page<SaleResponse> listActiveSales(Authentication authentication, Pageable pageable);

    public SaleResponse saleResponse(Long id, Authentication authentication);

    public SaleResponse createSale(@Valid SaleData saleData);

    public void deactiveSale(Long id);

    public Sale validSale(Long id);
}
