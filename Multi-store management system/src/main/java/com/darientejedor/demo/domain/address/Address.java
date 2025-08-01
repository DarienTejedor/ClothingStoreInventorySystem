package com.darientejedor.demo.domain.address;

import com.darientejedor.demo.domain.dtos.AddressData;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String city;
    private String locality;
    private String street;

    public Address(AddressData addresData) {
        this.city = addresData.city();
        this.locality = addresData.locality();
        this.street = addresData.street();
    }
}
