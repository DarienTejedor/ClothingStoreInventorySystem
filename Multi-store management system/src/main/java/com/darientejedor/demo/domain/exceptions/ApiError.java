package com.darientejedor.demo.domain.exceptions;

public record ApiError(
        int status,
        String error,
        String message
) {
}
