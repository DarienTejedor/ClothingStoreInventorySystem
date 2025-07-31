package com.darientejedor.demo.domain.salesdetails;

import com.darientejedor.demo.address.Address;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.sales.Sale;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;

import java.math.BigDecimal;

@Table(name = "sale_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sale_id", "product_id"})
})
@Entity(name = "SaleDetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "unit_price",nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Long quantity;
}
