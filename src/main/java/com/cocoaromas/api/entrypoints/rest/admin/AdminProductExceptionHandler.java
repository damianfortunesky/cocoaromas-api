package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.service.admin.AdminProductNotFoundException;
import com.cocoaromas.api.application.service.admin.AdminProductValidationException;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminProductExceptionHandler {

    @ExceptionHandler(AdminProductValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(AdminProductValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(AdminProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AdminProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("PRODUCT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleDtoValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Request inválido");
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", message));
    }
}
