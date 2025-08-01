package com.darientejedor.demo.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PasswordUpdateData(
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword

) {
}
