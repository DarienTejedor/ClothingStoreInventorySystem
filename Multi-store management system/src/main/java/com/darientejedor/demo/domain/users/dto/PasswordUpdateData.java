package com.darientejedor.demo.domain.users.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordUpdateData(
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword

) {
}
