package com.darientejedor.demo.domain.stores;

import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.stores.dto.StoreData;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "stores")
@Entity(name = "Store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Embedded
    @Column(unique = true, nullable = false)
    private Address address;
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String email;

    public Store(StoreData storeData) {
        this.name = storeData.name();
        this.address = storeData.address();
        this.phoneNumber = storeData.phoneNumber();
        this.email = storeData.email();
    }
}

