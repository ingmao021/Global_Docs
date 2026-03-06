package com.empresa.docprocessor.domain.exception;

/**
 * Se lanza cuando falla la validación del documento (identificador tributario, prefijo, matriz tipo-formato).
 */
public final class ValidationException extends DocumentProcessingException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
