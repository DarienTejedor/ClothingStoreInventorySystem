package com.darientejedor.demo.domain.inventory;

import com.darientejedor.demo.address.Address;
import com.darientejedor.demo.domain.products.Product;
import com.darientejedor.demo.domain.stores.Store;
import jakarta.persistence.*;
import lombok.*;


@Table(name = "inventories", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "store_id"})
})
@Entity(name = "Inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false)
    private Long stock;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}