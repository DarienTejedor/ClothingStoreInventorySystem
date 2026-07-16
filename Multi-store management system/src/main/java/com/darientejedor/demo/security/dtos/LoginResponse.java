package com.darientejedor.demo.security.dtos;

public record LoginResponse(
        String token,
        String refreshToken,
        Long id,
        String name,
        String role
) {
}
