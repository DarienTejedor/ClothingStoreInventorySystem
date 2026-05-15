package com.darientejedor.demo.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> notFound(EntityNotFoundException e){
        ApiError error = new ApiError(404, "Not Found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> badRequest(ValidationException e){
        ApiError error = new ApiError(400, "Bad Request", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> illegalArgument(IllegalArgumentException e){
        ApiError error = new ApiError(400, "Bad Request", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Parameter '" + e.getName() + "' is invalid. Value provided: '" + e.getValue() + "'.";
        ApiError error = new ApiError(400, "Bad Request", message);
        return ResponseEntity.badRequest().body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> unAuthenticated(UnauthorizedException e) {
        ApiError error = new ApiError(401, "No autenticado", "Falta el token o es inválido");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> accessDenied(ForbiddenException e) {
        ApiError error = new ApiError(403, "Sin permisos", "No tienes acceso a este recurso");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}


