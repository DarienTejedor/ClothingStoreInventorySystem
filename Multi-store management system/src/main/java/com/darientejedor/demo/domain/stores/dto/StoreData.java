package com.darientejedor.demo.domain.stores.dto;

import com.darientejedor.demo.domain.address.Address;
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
