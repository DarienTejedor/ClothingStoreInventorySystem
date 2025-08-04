package com.darientejedor.demo.domain.stores.dto;

import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.stores.Store;

public record StoreResponse(
        Long id,
        String name,
        Address address,
        String phoneNumber,
        String email
) {
    public StoreResponse(Store store){
        this(store.getId(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getEmail());
    }
}
