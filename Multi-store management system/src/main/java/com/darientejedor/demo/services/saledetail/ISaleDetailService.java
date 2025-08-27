package com.darientejedor.demo.services.saledetail;

import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailData;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailResponse;

public interface ISaleDetailService {

    SaleDetailResponse addSaleDetail(Long saleId, SaleDetailData saleDetailData);
}
