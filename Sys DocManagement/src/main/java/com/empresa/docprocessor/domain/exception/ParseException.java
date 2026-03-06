package com.empresa.docprocessor.domain.exception;

/**
 * Se lanza cuando falla el parseo o lectura del contenido del documento.
 */
public final class ParseException extends DocumentProcessingException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
