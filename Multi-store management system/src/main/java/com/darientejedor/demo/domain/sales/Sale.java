package com.darientejedor.demo.domain.sales;

import com.darientejedor.demo.domain.sales.dto.SaleData;
import com.darientejedor.demo.domain.stores.Store;
import com.darientejedor.demo.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "sales")
@Entity(name = "Sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;
    @Column(name = "total_sale", nullable = false)
    private BigDecimal totalSale;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private boolean active;

    public Sale(SaleData saleData, Store store, User user){
        this.saleDate = LocalDateTime.now();
        this.totalSale = BigDecimal.ZERO;
        this.store = store;
        this.user = user;
        this.active = true;
    }


    public void deactiveSale(){
        this.active = false;
    }

}








