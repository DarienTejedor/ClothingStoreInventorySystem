package com.darientejedor.demo.services.product;

import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.products.dtos.ProductData;
import com.darientejedor.demo.domain.products.dtos.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    public Page<ProductResponse> listActiveProducts(Pageable pageable);

    public ProductResponse productResponse(Long id);

    public ProductResponse createProduct(@Valid ProductData productData);

    public ProductResponse updateProduct(Long id, @Valid ProductData productData);

    public void deactiveProduct(Long id);

    public Product validProduct(Long id);
}
