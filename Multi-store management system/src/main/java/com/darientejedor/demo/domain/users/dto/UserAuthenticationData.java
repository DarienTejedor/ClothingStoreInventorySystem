package com.darientejedor.demo.domain.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationData(
        @NotBlank
        String loginUser,
        @NotBlank
        String password
) {
}

// DTO pide login y clave
