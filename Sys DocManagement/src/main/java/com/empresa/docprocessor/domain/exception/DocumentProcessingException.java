package com.empresa.docprocessor.domain.exception;

/**
 * Excepción base (checked) para todos los errores de procesamiento de documentos.
 */
public sealed class DocumentProcessingException extends Exception
        permits ValidationException, UnsupportedFormatException, ParseException, BatchProcessingException {

    public DocumentProcessingException(String message) {
        super(message);
    }

    public DocumentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
