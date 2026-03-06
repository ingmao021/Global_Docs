package com.empresa.docprocessor.domain.model;

import java.nio.file.Path;

/**
 * Representa un documento a procesar con su tipo, formato y país de origen.
 */
public record Document(
        String fileName,
        Path filePath,
        DocumentType documentType,
        DocumentFormat documentFormat,
        Country country
) {
    /**
     * Crea un documento con ruta nula (útil para pruebas o cuando solo se tiene el nombre).
     */
    public static Document of(String fileName, DocumentType documentType, DocumentFormat documentFormat, Country country) {
        return new Document(fileName, null, documentType, documentFormat, country);
    }
}
