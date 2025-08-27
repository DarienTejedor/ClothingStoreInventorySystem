package com.darientejedor.demo.services.sale;

import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ISaleService {
    Page<SaleResponse> listActiveSales(Authentication authentication, Pageable pageable);

    SaleResponse saleResponse(Long id, Authentication authentication);

    SaleResponse createSale(@Valid SaleData saleData);

    void deactiveSale(Long id);
}
