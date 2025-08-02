package com.darientejedor.demo.domain.products;

import com.darientejedor.demo.domain.products.dtos.ProductData;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "products")
@Entity(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigDecimal price;

    public Product(ProductData productData){
        this.name = productData.name();
        this.description = productData.description();
        this.price = productData.price();
    }
}