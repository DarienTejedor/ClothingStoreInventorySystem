package com.darientejedor.demo.domain.roles;

import jakarta.validation.constraints.NotBlank;

public record RoleData(
        @NotBlank
        String name
) {
}
