package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.service.admin.AdminStockValidationException;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminStockExceptionHandler {

    @ExceptionHandler(AdminStockValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(AdminStockValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }
}
