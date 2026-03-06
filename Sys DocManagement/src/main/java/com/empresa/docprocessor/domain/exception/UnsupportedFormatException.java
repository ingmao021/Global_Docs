package com.empresa.docprocessor.domain.exception;

/**
 * Se lanza cuando el formato del documento no es soportado o no es compatible con el tipo.
 */
public final class UnsupportedFormatException extends DocumentProcessingException {

    public UnsupportedFormatException(String message) {
        super(message);
    }

    public UnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
