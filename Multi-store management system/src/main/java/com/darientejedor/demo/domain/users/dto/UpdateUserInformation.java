package com.darientejedor.demo.domain.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserInformation(
        @NotBlank
        String name,
        @NotNull
        Long document
) {
}
