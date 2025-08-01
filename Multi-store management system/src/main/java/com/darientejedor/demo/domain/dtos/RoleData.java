package com.darientejedor.demo.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record RoleData(
        @NotBlank
        String name
) {
}
