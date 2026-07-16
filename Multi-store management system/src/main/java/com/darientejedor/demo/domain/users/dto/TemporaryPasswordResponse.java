package com.darientejedor.demo.domain.users.dto;

public record TemporaryPasswordResponse(
        String loginUser,
        String temporaryPassword
) {
}
