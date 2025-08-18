package com.darientejedor.demo.controller.saledetail;


import com.darientejedor.demo.domain.sales.dto.SaleResponse;
import com.darientejedor.demo.domain.salesdetails.SaleDetail;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailData;
import com.darientejedor.demo.domain.salesdetails.dto.SaleDetailResponse;
import com.darientejedor.demo.services.sale.SaleService;
import com.darientejedor.demo.services.saledetail.ISaleDetailService;
import com.darientejedor.demo.services.saledetail.SaleDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/sale/{saleId}/details")
@Tag(name = "SalesDetails", description = "Endpoints for managing sales details in the system.")
public class SaleDetailController {


    private final ISaleDetailService saleDetailService;

    public SaleDetailController(ISaleDetailService saleDetailService) {
        this.saleDetailService = saleDetailService;
    }

    @Operation(
            summary = "Create a new sale details",
            description = "Creates a new sale detail for a specific sale, updating the inventory stock and the total sale amount.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Sale details created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SaleDetailResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sale not found, product not found, or inventory not found for the specified store.",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input.",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    @Transactional
    public ResponseEntity<SaleDetailResponse> addSaleDetail(@PathVariable Long saleId, @RequestBody @Valid SaleDetailData saleDetailData) {
        SaleDetailResponse saleDetail = saleDetailService.addSaleDetail(saleId, saleDetailData);
        URI ubication = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saleDetail.id())
                .toUri();
        return ResponseEntity.created(ubication).body(saleDetail);
    }
}
