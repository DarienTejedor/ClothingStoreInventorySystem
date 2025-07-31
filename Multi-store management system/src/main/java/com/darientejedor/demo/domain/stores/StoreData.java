package com.darientejedor.demo.domain.stores;

import com.darientejedor.demo.address.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoreData(
        @NotBlank
        String name,
        @NotNull
        Address address,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String email
) {
}
