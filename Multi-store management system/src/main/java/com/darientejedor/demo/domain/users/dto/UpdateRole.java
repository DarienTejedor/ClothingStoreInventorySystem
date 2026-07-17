package com.darientejedor.demo.domain.users.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateRole(
        @NotNull
        Long roleId
) {
}
