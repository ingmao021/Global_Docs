package com.empresa.docprocessor.domain.parser;

import com.empresa.docprocessor.domain.exception.DocumentProcessingException;
import com.empresa.docprocessor.domain.model.Document;

/**
 * Contrato para parsers de documentos por formato (PDF, DOCX, XLSX, CSV, TXT).
 * Las implementaciones simulan el procesamiento sin leer contenido real del archivo.
 */
public interface DocumentParser {

    /**
     * Procesa (parsea) el documento. En esta versión se simula con un delay;
     * no se lee el contenido real del archivo.
     *
     * @param document documento a parsear
     * @throws com.empresa.docprocessor.domain.exception.ParseException cuando el parseo falla
     */
    void parse(Document document) throws DocumentProcessingException;
}
