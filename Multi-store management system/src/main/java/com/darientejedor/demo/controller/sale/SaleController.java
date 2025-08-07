package com.darientejedor.demo.controller.sale;


import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import com.darientejedor.demo.services.sale.SaleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> saleList(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(saleService.listActiveSales(pageable).map(SaleResponse::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> saleResponse(@PathVariable Long id){
        return ResponseEntity.ok(saleService.saleResponse(id));
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@RequestBody @Valid SaleData saleData){
        SaleResponse saleResponse = saleService.createSale(saleData);
        return ResponseEntity.ok(saleResponse);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deactiveSale(@PathVariable Long id){
        saleService.deactiveSale(id);
        return ResponseEntity.noContent().build();
    }
}


















