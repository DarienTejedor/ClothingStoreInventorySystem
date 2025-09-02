package com.darientejedor.demo.services.product;

import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.products.dtos.ProductData;
import com.darientejedor.demo.domain.products.dtos.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<ProductResponse> listActiveProducts(Pageable pageable);

    ProductResponse createProduct(@Valid ProductData productData);

    ProductResponse updateProduct(Long id, @Valid ProductData productData);

    void deactiveProduct(Long id);
}
