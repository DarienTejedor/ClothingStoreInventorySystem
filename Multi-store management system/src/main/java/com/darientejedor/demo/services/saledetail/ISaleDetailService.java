package com.darientejedor.demo.services.saledetail;

import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailData;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailResponse;

public interface ISaleDetailService {

    public SaleDetailResponse addSaleDetail(Long saleId, SaleDetailData saleDetailData);
}
