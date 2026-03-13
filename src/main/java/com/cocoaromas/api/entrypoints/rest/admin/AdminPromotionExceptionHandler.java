package com.cocoaromas.api.entrypoints.rest.admin;

import com.cocoaromas.api.application.service.admin.AdminPromotionNotFoundException;
import com.cocoaromas.api.application.service.admin.AdminPromotionValidationException;
import com.cocoaromas.api.entrypoints.rest.admin.AdminPromotionDtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminPromotionExceptionHandler {

    @ExceptionHandler(AdminPromotionValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(AdminPromotionValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(AdminPromotionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AdminPromotionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("PROMOTION_NOT_FOUND", ex.getMessage()));
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
