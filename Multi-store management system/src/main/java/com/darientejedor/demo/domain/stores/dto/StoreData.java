package com.darientejedor.demo.domain.stores.dto;

import com.darientejedor.demo.domain.address.Address;
import com.darientejedor.demo.domain.address.dtos.AddressData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoreData(
        @NotBlank
        String name,
        @NotNull @Valid
        AddressData address,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String email
) {
}
