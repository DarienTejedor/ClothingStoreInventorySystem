package com.darientejedor.demo.domain.stores;

import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.stores.dto.StoreData;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "stores")
@Entity(name = "Store")
@Setter
@Getter
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
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private boolean active;

    public Store(StoreData storeData) {
        this.name = storeData.name();
        this.address = new Address(storeData.address());
        this.phoneNumber = storeData.phoneNumber();
        this.email = storeData.email();
        this.active = true;
    }

    public void deactiveStore() {
        this.active = false;
    }
}

