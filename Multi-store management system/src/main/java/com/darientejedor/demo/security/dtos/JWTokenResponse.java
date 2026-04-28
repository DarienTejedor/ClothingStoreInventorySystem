package com.darientejedor.demo.security.dtos;

public record JWTokenResponse(
        String token,
        String name,
        String role
) {
}
