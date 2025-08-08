package com.darientejedor.demo.services.product;

import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.products.dtos.ProductData;
import com.darientejedor.demo.domain.products.dtos.ProductResponse;
import com.darientejedor.demo.domain.products.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public Page<Product> listActiveProducts(Pageable pageable) { return productRepository.findByActiveTrue(pageable);
    }


    public ProductResponse productResponse(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(("Product not found with ID: " + id)));
        if (!product.isActive()){
            throw new IllegalArgumentException("Product not found or inactive with ID: " + id);
        } return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }


    public void createProduct(@Valid ProductData productData) {
        if (productRepository.findByName(productData.name()).isPresent()){
            throw new IllegalArgumentException("Store with this name already exists: " + productData.name());
        }
        var product = new Product(productData);
        productRepository.save(product);
    }


    public ProductResponse updateProduct(Long id, @Valid ProductData productData) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(("Product not found with ID: " + id)));
        if (!product.isActive()){
            throw new IllegalArgumentException("Product not found or inactive with ID: " + id);
        }
        product.setName(productData.name());
        product.setDescription(productData.description());
        product.setPrice(productData.price());
        productRepository.save(product);
        return new ProductResponse(product);
    }

    public void deactiveProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(("Product not found with ID: " + id)));
        if (!product.isActive()){
            throw new IllegalArgumentException("Product not found or already inactive with ID: " + id);
        }
        product.deactiveProduct();
        productRepository.save(product);
    }
}
