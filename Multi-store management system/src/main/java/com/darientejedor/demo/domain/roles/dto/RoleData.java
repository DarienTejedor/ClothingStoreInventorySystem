package com.darientejedor.demo.domain.roles.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleData(
        @NotBlank
        String name
) {
}
