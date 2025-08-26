package com.darientejedor.demo.services.product;

import com.darientejedor.demo.domain.inventory.Inventory;
import com.darientejedor.demo.domain.inventory.repository.InventoryRepository;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.products.dtos.ProductData;
import com.darientejedor.demo.domain.products.dtos.ProductResponse;
import com.darientejedor.demo.domain.products.repository.ProductRepository;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.services.inventory.IInventoryService;
import com.darientejedor.demo.services.user.authentications.IUserAuthentications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final IUserAuthentications userAuthentications;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository, IUserAuthentications userAuthentications, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.userAuthentications = userAuthentications;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Page<ProductResponse> listActiveProducts(Authentication authentication, Pageable pageable) {
        User authUser = userAuthentications.authUser(authentication);
        String authRole = userAuthentications.authRole(authentication);

        if ("ROLE_GENERAL_ADMIN".equals(authRole)) {
            return productRepository.findByActiveTrue(pageable).map(ProductResponse::new);
        }
        else {
            Long storeId = authUser.getStore().getId();
            Page<Inventory> productInventory = inventoryRepository.findByStoreId(storeId, pageable);
            return productInventory.map(Inventory::getProduct).map(ProductResponse::new);
        }
    }



    @Override
    public ProductResponse createProduct(@Valid ProductData productData) {
        if (productRepository.findByName(productData.name()).isPresent()){
            throw new ValidationException("Store with this name already exists: " + productData.name());
        }
        var product = new Product(productData);
        productRepository.save(product);
        return new ProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, @Valid ProductData productData) {
        Product product = validProduct(id);
        product.setName(productData.name());
        product.setDescription(productData.description());
        product.setPrice(productData.price());
        productRepository.save(product);
        return new ProductResponse(product);
    }

    @Override
    public void deactiveProduct(Long id) {
        Product product = validProduct(id);
        product.deactiveProduct();
        productRepository.save(product);
    }

    @Override
    public Product validProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(("Product not found with ID: " + id)));
        if (!product.isActive()){
            throw new ValidationException("Product not found or inactive with ID: " + id);
        }
        return product;
    }
}
