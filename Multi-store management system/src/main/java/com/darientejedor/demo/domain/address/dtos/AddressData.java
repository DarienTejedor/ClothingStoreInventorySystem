package com.darientejedor.demo.domain.address.dtos;


import jakarta.validation.constraints.NotBlank;

public record AddressData(
        @NotBlank
        String city,
        @NotBlank
        String locality,
        @NotBlank
        String street

) {

}
