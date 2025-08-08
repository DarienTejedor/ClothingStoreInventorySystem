package com.darientejedor.demo.controller.product;

import com.darientejedor.demo.domain.products.dtos.ProductData;
import com.darientejedor.demo.domain.products.dtos.ProductResponse;
import com.darientejedor.demo.services.product.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> productList(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(productService.listActiveProducts(pageable).map(ProductResponse::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> productResponse(@PathVariable Long id){
        return ResponseEntity.ok(productService.productResponse(id));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductData productData){
        productService.createProduct(productData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductData productData){
        ProductResponse productResponse = productService.updateProduct(id, productData);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deactiveProduct(id);
        return ResponseEntity.noContent().build();
    }
}
