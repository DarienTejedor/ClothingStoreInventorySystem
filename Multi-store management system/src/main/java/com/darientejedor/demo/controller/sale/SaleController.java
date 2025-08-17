package com.darientejedor.demo.controller.sale;


import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import com.darientejedor.demo.services.sale.ISaleService;
import com.darientejedor.demo.services.sale.SaleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/sales")
public class SaleController {


    private final ISaleService saleService;

    public SaleController(ISaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> saleList(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(saleService.listActiveSales(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> saleResponse(@PathVariable Long id){
        return ResponseEntity.ok(saleService.saleResponse(id));
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@RequestBody @Valid SaleData saleData){
        SaleResponse sale = saleService.createSale(saleData);
        URI ubication = URI.create("/sales/" + sale.id());
        return ResponseEntity.created(ubication).body(sale);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deactiveSale(@PathVariable Long id){
        saleService.deactiveSale(id);
        return ResponseEntity.noContent().build();
    }
}


















