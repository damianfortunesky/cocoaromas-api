package com.cocoaromas.tienda_api.common.exception;


// Error de negocio (validaciones, reglas, credenciales, etc.).
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
