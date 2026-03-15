package com.cocoaromas.api.entrypoints.rest.admin.category;

import com.cocoaromas.api.application.service.admin.category.AdminCategoryNotFoundException;
import com.cocoaromas.api.application.service.admin.category.AdminCategoryValidationException;
import com.cocoaromas.api.entrypoints.rest.admin.category.AdminCategoryDtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminCategoryExceptionHandler {

    @ExceptionHandler(AdminCategoryValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(AdminCategoryValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(AdminCategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AdminCategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("CATEGORY_NOT_FOUND", ex.getMessage()));
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
