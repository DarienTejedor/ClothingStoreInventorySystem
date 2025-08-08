package com.darientejedor.demo.controller.saledetail;


import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailData;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailResponse;
import com.darientejedor.demo.services.sale.SaleService;
import com.darientejedor.demo.services.saledetail.SaleDetailService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sale/{saleId}/details")
public class SaleDetailController {

    @Autowired
    private SaleDetailService saleDetailService;

    @PostMapping
    @Transactional
    public ResponseEntity<SaleDetailResponse> addSaleDetail(@PathVariable Long saleId, @RequestBody @Valid SaleDetailData saleDetailData) {
        SaleDetailResponse saleDetailResponse = saleDetailService.addSaleDetail(saleId, saleDetailData);
        return ResponseEntity.ok(saleDetailResponse);
    }
}
