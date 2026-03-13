package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.service.admin.AdminOrderNotFoundException;
import com.cocoaromas.api.application.service.admin.AdminOrderValidationException;
import com.cocoaromas.api.entrypoints.rest.admin.AdminProductDtos.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminOrderExceptionHandler {

    @ExceptionHandler(AdminOrderValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(AdminOrderValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(AdminOrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AdminOrderNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponse("ORDER_NOT_FOUND", ex.getMessage()));
    }
}
