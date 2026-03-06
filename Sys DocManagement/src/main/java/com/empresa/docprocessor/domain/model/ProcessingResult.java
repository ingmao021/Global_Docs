package com.empresa.docprocessor.domain.model;

/**
 * Resultado del procesamiento de un documento (éxito o fallo con mensaje).
 */
public record ProcessingResult(
        String fileName,
        Country country,
        DocumentType documentType,
        DocumentFormat documentFormat,
        boolean success,
        String message,
        long processingTimeMs
) {
    /**
     * Resultado exitoso.
     */
    public static ProcessingResult success(String fileName, Country country, DocumentType documentType,
                                           DocumentFormat documentFormat, long processingTimeMs) {
        return new ProcessingResult(fileName, country, documentType, documentFormat,
                true, "Procesado correctamente", processingTimeMs);
    }

    /**
     * Resultado con error.
     */
    public static ProcessingResult failure(String fileName, Country country, DocumentType documentType,
                                           DocumentFormat documentFormat, String message, long processingTimeMs) {
        return new ProcessingResult(fileName, country, documentType, documentFormat,
                false, message != null ? message : "Error desconocido", processingTimeMs);
    }
}
