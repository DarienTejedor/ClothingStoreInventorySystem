package com.darientejedor.demo.address;


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
