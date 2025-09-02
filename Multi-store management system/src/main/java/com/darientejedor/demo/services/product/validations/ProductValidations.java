package com.darientejedor.demo.services.product.validations;

import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.products.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

public class ProductValidations implements IProductValidations{

    private final ProductRepository productRepository;

    public ProductValidations(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product validProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(("Product not found with ID: " + id)));
        if (!product.isActive()){
            throw new ValidationException("Product not found or inactive with ID: " + id);
        }
        return product;
    }
}
