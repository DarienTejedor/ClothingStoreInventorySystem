package com.darientejedor.demo.services.sale.validations;

import com.darientejedor.demo.domain.sales.Sale;
import com.darientejedor.demo.domain.stores.Store;

public interface ISaleValidations {
    Sale validSale(Long saleId);
}
