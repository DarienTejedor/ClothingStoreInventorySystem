package com.darientejedor.demo.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationData(
        @NotBlank
        String loginUser,
        @NotBlank
        String password
) {
}

// DTO pide login y clave
