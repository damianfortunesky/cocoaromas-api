package com.cocoaromas.api.entrypoints.rest.user;

import com.cocoaromas.api.application.service.user.MyAddressNotFoundException;
import com.cocoaromas.api.application.service.user.MyProfileValidationException;
import com.cocoaromas.api.entrypoints.rest.user.UserProfileDtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserProfileExceptionHandler {

    @ExceptionHandler(MyProfileValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MyProfileValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(MyAddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFound(MyAddressNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("ADDRESS_NOT_FOUND", ex.getMessage()));
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
