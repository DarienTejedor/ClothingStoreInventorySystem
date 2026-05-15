package com.darientejedor.demo.exceptions;

public record ApiError(
        int status,
        String error,
        String message
) {
}
