package com.empresa.docprocessor.domain.exception;

/**
 * Se lanza cuando falla el procesamiento por lotes (chunk, hilos, etc.).
 */
public final class BatchProcessingException extends DocumentProcessingException {

    public BatchProcessingException(String message) {
        super(message);
    }

    public BatchProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
