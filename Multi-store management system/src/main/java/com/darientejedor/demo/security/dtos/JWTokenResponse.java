package com.darientejedor.demo.security.dtos;

public record JWTokenResponse(
        String token,
        String refreshToken,
        String name,
        String role
) {
}
